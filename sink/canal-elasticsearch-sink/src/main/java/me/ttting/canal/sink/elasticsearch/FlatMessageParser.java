package me.ttting.canal.sink.elasticsearch;

import java.util.Map;

/**
 * Created by jiangtiteng on 2018/10/18
 */
public interface FlatMessageParser {
    String parsePrimaryKey(FlatMessage flatMessage, Map<String, String> data);

    Map<String, String> parseSource(FlatMessage flatMessage, Map<String, String> data);

    String parseIndex(FlatMessage flatMessage);

    String parseType(FlatMessage flatMessage);

    boolean isConfigured(FlatMessage flatMessage);
}
