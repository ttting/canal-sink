package me.ttting.canal.node;

import me.ttting.canal.Channel;
import me.ttting.canal.Sink;
import me.ttting.canal.Source;
import me.ttting.canal.channel.MemoryChannel;
import me.ttting.canal.conf.Configurable;
import me.ttting.canal.sink.DefaultSinkFactory;
import me.ttting.canal.source.DefaultSourceFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Properties;

/**
 * Created by jiangtiteng on 2018/10/19
 */
public class YamlFileConfigurationProvider implements ConfigurationProvider {
    private String filePath;

    private Yaml yaml;

    private Map<String, Object> config;

    private Channel channel;

    private Source source;

    private Sink sink;

    private DefaultSourceFactory defaultSourceFactory;

    private DefaultSinkFactory defaultSinkFactory;

    private String CLASSPATH_PREFIX = "classpath:";

    public YamlFileConfigurationProvider(String filePath) {
        this.filePath = filePath;
        this.yaml = new Yaml();
        this.defaultSourceFactory = new DefaultSourceFactory();
        this.defaultSinkFactory = new DefaultSinkFactory();
    }

    public void load() {
        try {
            if (this.filePath.startsWith(CLASSPATH_PREFIX)) {
                String subFilePath = this.filePath.substring(CLASSPATH_PREFIX.length(), this.filePath.length());
                config = (Map<String, Object>) yaml.load(YamlFileConfigurationProvider.class.getClassLoader().getResourceAsStream(subFilePath));
            } else {
                config = (Map<String, Object>) yaml.load(new FileInputStream(filePath));
            }
            loadChannel();
            loadSink();
            loadSource();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void loadChannel() {
        Map<String, String> channelConfig = (Map<String, String>) config.get("channel");

        Properties config = new Properties();
        config.putAll(channelConfig);

        MemoryChannel memoryChannel = new MemoryChannel();
        if (memoryChannel instanceof Configurable)
            memoryChannel.configure(config);

        this.channel = memoryChannel;

    }

    private void loadSource() {
        Map<String, Object> sourceConfig = (Map<String, Object>) config.get("source");
        if (sourceConfig == null)
            throw new IllegalArgumentException("source config must be special");

        Properties config = new Properties();
        config.putAll(sourceConfig);

        String sourceType = (String) config.get("type");
        String sourceName = (String) config.get("name");
        Source source = defaultSourceFactory.create(sourceName, sourceType);

        if (source instanceof Configurable) {
            ((Configurable) source).configure(config);
        }

        source.setChannel(getChannel());
        this.source = source;
    }

    private void loadSink() {
        Map<String, Object> sinkConfig = (Map<String, Object>) config.get("sink");
        if (sinkConfig == null)
            throw new IllegalArgumentException("sink config must be special");

        Properties config = new Properties();
        config.putAll(sinkConfig);

        String sinkType = (String) config.get("type");
        String sinkName = (String) config.get("name");
        Sink sink = defaultSinkFactory.create(sinkName, sinkType);

        if (sink instanceof Configurable) {
            ((Configurable) sink).configure(config);
        }
        sink.setChannel(getChannel());
        this.sink = sink;
    }

    @Override
    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public Source getSource() {
        return this.source;
    }

    @Override
    public Sink getSink() {
        return this.sink;
    }

    public static void main(String[] args) {
        YamlFileConfigurationProvider yamlFileConfigurationProvider = new YamlFileConfigurationProvider("classpath:server.yaml");
        yamlFileConfigurationProvider.load();
    }
}
