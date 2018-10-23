package me.ttting.canal.source;

import java.util.Properties;

/**
 * Created by jiangtiteng on 2018/10/19
 */
public abstract class AbstractPollableSource extends AbstractSource implements PollableSource {
    @Override
    public void configure(Properties properties) {
       super.configure(properties);
    }

    @Override
    public long getBackOffSleepIncrement() {
        return 0;
    }

    @Override
    public long getMaxBackOffSleepInterval() {
        return 0;
    }
}
