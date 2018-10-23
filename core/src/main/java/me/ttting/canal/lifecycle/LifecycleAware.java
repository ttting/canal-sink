package me.ttting.canal.lifecycle;

/**
 * Created by jiangtiteng on 2018/10/8
 */
public interface LifecycleAware {
    void start();

    void stop();

    LifecycleState getLifeCycleState();
}
