package me.ttting.canal.source;

import me.ttting.canal.Channel;
import me.ttting.canal.NamedComponent;
import me.ttting.canal.Source;
import me.ttting.canal.conf.Configurable;
import me.ttting.canal.lifecycle.LifecycleAware;
import me.ttting.canal.lifecycle.LifecycleState;

import java.util.Properties;

/**
 * Created by jiangtiteng on 2018/10/9
 */
public abstract class AbstractSource implements Source, LifecycleAware , Configurable {
    private String name;

    private LifecycleState lifecycleState;

    private Channel channel;

    @Override
    public void configure(Properties properties) {

    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public LifecycleState getLifeCycleState() {
        return lifecycleState;
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
