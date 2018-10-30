package me.ttting.canal.channel;

import me.ttting.canal.Channel;
import me.ttting.canal.conf.Configurable;
import me.ttting.canal.event.Event;
import me.ttting.canal.lifecycle.LifecycleState;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by jiangtiteng on 2018/10/17
 */
public class MemoryChannel implements Channel, Configurable {
    BlockingDeque<Event> eventList;

    public MemoryChannel() {
        eventList = new LinkedBlockingDeque();
    }

    @Override
    public void configure(Properties properties) {

    }

    @Override
    public void put(Event event) {
        try {
            eventList.put(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Event take() {
        return eventList.poll();
    }

    @Override
    public void batchPut(List<Event> events) {
        eventList.addAll(events);
    }

    @Override
    public void start() {}

    @Override
    public void stop() {

    }

    @Override
    public LifecycleState getLifeCycleState() {
        return null;
    }
}
