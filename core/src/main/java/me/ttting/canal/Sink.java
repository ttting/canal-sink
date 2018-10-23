package me.ttting.canal;

import me.ttting.canal.lifecycle.LifecycleAware;

/**
 * Created by jiangtiteng on 2018/10/8
 */
public interface Sink extends LifecycleAware, NamedComponent{
    /**
     * Set the channel the sink will consume from
     * @param chanel The Channel to be polled;
     */
    void setChannel(Channel chanel);

    /**
     * @return the channel associated with this sink
     */
    Channel getChannel();

    Status process();

    enum Status {
        READY, BACKOFF
    }
}
