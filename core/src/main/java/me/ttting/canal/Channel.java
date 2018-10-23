package me.ttting.canal;


import me.ttting.canal.event.Event;
import me.ttting.canal.lifecycle.LifecycleAware;

import java.util.List;

/**
 * Created by jiangtiteng on 2018/10/8
 */
public interface Channel extends LifecycleAware {
    void put(Event event);

    void batchPut(List<Event> events);

    Event take();
}
