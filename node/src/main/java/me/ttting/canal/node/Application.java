package me.ttting.canal.node;

import com.google.common.base.Preconditions;
import me.ttting.canal.Channel;
import me.ttting.canal.Sink;
import me.ttting.canal.Source;
import me.ttting.canal.channel.MemoryChannel;
import me.ttting.canal.lifecycle.LifecycleAware;
import me.ttting.canal.sink.PollableSinkRunner;
import me.ttting.canal.sink.elasticsearch.ElasticsearchSink;
import me.ttting.canal.source.PollableSource;
import me.ttting.canal.source.PollableSourceRunner;
import me.ttting.canal.source.kafka.CanalKafkaSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.yaml.snakeyaml.Yaml;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jiangtiteng on 2018/10/9
 */
@SpringBootApplication
public class Application implements CommandLineRunner {

    YamlFileConfigurationProvider yamlFileConfigurationProvider;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    List<LifecycleAware> components = new LinkedList<>();

    @Override
    public void run(String... args) throws Exception {
        String conf = System.getProperty("server.yaml", "classpath:server.yaml");
        Preconditions.checkNotNull(conf, "conf must not be null");

        yamlFileConfigurationProvider = new YamlFileConfigurationProvider(conf);
        yamlFileConfigurationProvider.load();

        statAllComponents();
    }


    public void statAllComponents() {
        Source source = yamlFileConfigurationProvider.getSource();

        PollableSourceRunner pollableSourceRunner = new PollableSourceRunner();
        pollableSourceRunner.setSource(source);

        components.add(pollableSourceRunner);


        Sink sink = yamlFileConfigurationProvider.getSink();

        PollableSinkRunner pollableSinkRunner = new PollableSinkRunner();
        pollableSinkRunner.setSink(sink);

        components.add(pollableSinkRunner);

        pollableSinkRunner.start();
        pollableSourceRunner.start();
    }
}
