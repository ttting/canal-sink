package me.ttting.canal;

import me.ttting.canal.Source;

/**
 * Created by jiangtiteng on 2018/10/19
 */
public interface SourceFactory {
    Source create(String sourceName, String type);

    Class<? extends Source> getClass(String type);
}
