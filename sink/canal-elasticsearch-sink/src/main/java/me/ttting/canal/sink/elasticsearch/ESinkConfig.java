package me.ttting.canal.sink.elasticsearch;

import lombok.Data;

import java.util.Map;

/**
 * Created by jiangtiteng on 2018/10/18
 */
@Data
public class ESinkConfig {
    private String database;

    private String table;

    private String primaryKeyName;

    private String index;

    private String type;

    private Map<String, String> filedMappings;
}
