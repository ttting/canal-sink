package me.ttting.canal.sink.elasticsearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import me.ttting.canal.Channel;
import me.ttting.canal.conf.Configurable;
import me.ttting.canal.event.Event;
import me.ttting.canal.sink.AbstractSink;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by jiangtiteng on 2018/10/17
 */
@Slf4j
public class ElasticsearchSink extends AbstractSink implements Configurable {

    private ObjectMapper objectMapper;

    private static final int defaultBatchSize = 5;

    private int batchSize = defaultBatchSize;

    private DocWriteRequestFactory docWriteRequestFactory;

    private RestHighLevelClient restHighLevelClient;

    private String hostNames;

    private String protocol;

    @Override
    public void configure(Properties properties) {
        try {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            Map<String, Object> config = (Map<String, Object>) properties.get("config");

            hostNames = (String) config.get(ElasticSearchSinkConstants.HOSTNAMES);
            protocol = (String) config.get(ElasticSearchSinkConstants.protocol);

            Preconditions.checkNotNull(hostNames, "hostNames");
            Preconditions.checkNotNull(protocol, "protocol");

            List<ESinkConfig> esSinkConfigs = objectMapper.readValue(objectMapper.writeValueAsString(config.get("esSinkConfig")), new TypeReference<List<ESinkConfig>>() {});
            docWriteRequestFactory = new DefaultDocWriteRequestFactory(new DefaultFlatMessageParser(esSinkConfigs));
            restHighLevelClient = ResetHighLevelClientFactory.createClient(hostNames, protocol);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Status process() {
        try {
            int count;
            Channel channel = getChannel();
            BulkRequest bulkRequest = new BulkRequest();

            for (count = 0; count < batchSize; count++) {
                Event event = channel.take();
                if (event == null)
                    break;
                FlatMessage flatMessage = objectMapper.readValue(event.getBody(), FlatMessage.class);
                List<DocWriteRequest> docWriteRequests = docWriteRequestFactory.buildWithFlatMessage(flatMessage);
                if (docWriteRequests != null && docWriteRequests.size() > 0) {
                    bulkRequest.add(docWriteRequests);
                }
            }

            if (bulkRequest.numberOfActions() > 0) {
                BulkResponse bulkResponse =  restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
                System.out.println(bulkResponse);
            } else {
                return Status.BACKOFF;
            }
        } catch (Exception e) {
            log.error("exception ", e);
            return Status.BACKOFF;
        }
        return Status.READY;
    }
}
