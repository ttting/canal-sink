package me.ttting.canal;

/**
 * Created by jiangtiteng on 2018/10/22
 */
public enum SinkType {
    ELASTICSEARCHSINK("me.ttting.canal.sink.elasticsearch.ElasticsearchSink"),

    OTHER("OTHER");

    private final String sinkClassName;

    SinkType(String sinkClassName) {
        this.sinkClassName = sinkClassName;
    }

    public String getSinkClassName() {
        return sinkClassName;
    }
}
