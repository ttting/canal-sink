package me.ttting.canal.sink.elasticsearch;

/**
 * Created by jiangtiteng on 2018/10/18
 */
public enum FlatMessageType {
    INSERT,

    UPDATE,

    DELETE,

    NOT_SUPPORT;

    public static FlatMessageType safeValueof(String name) {
        try {
            return valueOf(name);
        } catch (Exception e) {
            return NOT_SUPPORT;
        }
    }
}
