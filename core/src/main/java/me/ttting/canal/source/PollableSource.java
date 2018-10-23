package me.ttting.canal.source;

import me.ttting.canal.Source;

/**
 * Created by jiangtiteng on 2018/10/19
 */
public interface PollableSource {
    long getBackOffSleepIncrement();

    long getMaxBackOffSleepInterval();

    Status process();

    static enum Status {
        READY, BACKOFF
    }
}
