package me.ttting.canal.node;

import me.ttting.canal.Channel;
import me.ttting.canal.Sink;
import me.ttting.canal.Source;

/**
 * Created by jiangtiteng on 2018/10/19
 */
public interface ConfigurationProvider {
    Channel getChannel();

    Source getSource();

    Sink getSink();
}
