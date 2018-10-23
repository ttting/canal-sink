package me.ttting.canal.sink.elasticsearch;

import org.elasticsearch.action.DocWriteRequest;

import java.util.List;

/**
 * Created by jiangtiteng on 2018/10/18
 */
public interface DocWriteRequestFactory {
    List<DocWriteRequest> buildWithFlatMessage(FlatMessage flatMessage);
}
