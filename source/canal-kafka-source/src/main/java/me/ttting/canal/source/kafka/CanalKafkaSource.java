package me.ttting.canal.source.kafka;

import com.google.common.base.Preconditions;
import me.ttting.canal.event.Event;
import me.ttting.canal.event.EventBuilder;
import me.ttting.canal.source.AbstractPollableSource;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.util.*;

/**
 * Created by jiangtiteng on 2018/10/9
 */
public class CanalKafkaSource extends AbstractPollableSource implements ConsumerRebalanceListener, me.ttting.canal.conf.Configurable {

    private final List<Event> eventList = new ArrayList<>();

    private KafkaConsumer<String, byte[]> kafkaConsumer;

    private Properties kafkaProps = new Properties();

    private String bootstrapServers;

    private String groupId;

    private String topic;

    private int maxBatchDurationMillis = 10001;

    private int batchUpperLimit = 1000;

    private Iterator<ConsumerRecord<String, byte[]>> it;

    @Override
    public Status process() {
        final String batchUUID = UUID.randomUUID().toString();
        byte[] eventBody;
        String kafkaKey;
        Event event;

        final long batchStartTime = System.currentTimeMillis();
        final long maxBatchEndTime = batchStartTime + maxBatchDurationMillis;

        try {
            while (eventList.size() < batchUpperLimit && System.currentTimeMillis() < maxBatchEndTime) {
                if (it == null || !it.hasNext()) {
                    ConsumerRecords<String, byte[]> consumerRecords = kafkaConsumer.poll(1000);
                    it = consumerRecords.iterator();
                    if (!it.hasNext()) {
                        break;
                    }
                }

                ConsumerRecord<String, byte[]> message = it.next();
                eventBody = message.value();
                event = EventBuilder.buildEvent(eventBody, null);
                eventList.add(event);
            }

            if (eventList.size() > 0) {
                getChannel().batchPut(eventList); eventList.clear();
            }

        } catch (Exception e) {
            return Status.BACKOFF;
        }

        return Status.READY;
    }

    @Override
    public void start() {
        initKafkaProps();
        kafkaConsumer = new KafkaConsumer(this.kafkaProps);
        kafkaConsumer.subscribe(Arrays.asList(this.topic), this);
    }

    @Override
    public void stop() {
        kafkaConsumer.close();
    }

    @Override
    public void configure(Properties properties) {
        HashMap<String, Object> config = (HashMap<String, Object>) properties.get("config");
        this.bootstrapServers = (String) config.get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG);
        this.groupId = (String) config.get(ConsumerConfig.GROUP_ID_CONFIG);
        this.topic = (String) config.get("topic");
        this.batchUpperLimit = (int) config.get("batchSize");
        this.maxBatchDurationMillis = (int) config.get("batchTimeout");

        Preconditions.checkNotNull(this.bootstrapServers, "bootstrap.servers must not be null");
        Preconditions.checkNotNull(this.groupId, "group.id must not be null");
        Preconditions.checkNotNull(this.topic, "topic must not be null");

    }

    private void initKafkaProps() {
        kafkaProps.clear();
        kafkaProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaSourceConstants.DEFAULT_KEY_DESERIALIZER);
        kafkaProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaSourceConstants.DEFAULT_VALUE_DESERIALIZER);
        kafkaProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        kafkaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    }

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {

    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {

    }
}
