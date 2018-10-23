package me.ttting.canal.sink;

import com.google.common.base.Preconditions;
import lombok.ToString;
import me.ttting.canal.Channel;
import me.ttting.canal.Sink;
import me.ttting.canal.lifecycle.LifecycleAware;
import me.ttting.canal.lifecycle.LifecycleState;

/**
 * Created by jiangtiteng on 2018/10/9
 */
@ToString
public abstract class AbstractSink implements Sink, LifecycleAware {
    private LifecycleState lifecycleState;

    private Channel channel;

    private String name;

    @Override
    public synchronized void start() {
        Preconditions.checkState(channel != null, "No channel configured");
        lifecycleState = LifecycleState.START;
    }

    @Override
    public synchronized void stop() {
        lifecycleState = LifecycleState.STOP;
    }

    @Override
    public synchronized void setChannel(Channel chanel) {
        this.channel = chanel;
    }

    @Override
    public synchronized Channel getChannel() {
        return this.channel;
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
}
