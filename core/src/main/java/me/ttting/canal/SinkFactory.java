package me.ttting.canal;

/**
 * Created by jiangtiteng on 2018/10/21
 */
public interface SinkFactory {
    Sink create(String sinkName, String type);

    Class<? extends Sink> getClass(String type);
}
