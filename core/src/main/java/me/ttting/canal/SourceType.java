package me.ttting.canal;

/**
 * Created by jiangtiteng on 2018/10/19
 */
public enum SourceType {
    CANALKAFKASOURCE("me.ttting.canal.source.kafka.CanalKafkaSource"),

    OTHER("OTHER");

    private final String sourceClassName;

    SourceType(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public String getSourceClassName() {return sourceClassName;}
}
