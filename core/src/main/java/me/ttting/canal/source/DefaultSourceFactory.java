package me.ttting.canal.source;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import me.ttting.canal.CannalSinkException;
import me.ttting.canal.Source;
import me.ttting.canal.SourceFactory;
import me.ttting.canal.SourceType;

/**
 * Created by jiangtiteng on 2018/10/19
 */
@Slf4j
public class DefaultSourceFactory implements SourceFactory {
    @Override
    public Source create(String sourceName, String type) {
        Preconditions.checkNotNull(sourceName, "sourceName");
        Preconditions.checkNotNull(type, "type");

        Class<? extends Source> sourceClass = getClass(type.toUpperCase());

        try {
            Source source = sourceClass.newInstance();
            source.setName(sourceName);
            return source;
        } catch (Exception e) {
            throw new CannalSinkException("Unable to create source: " + sourceName + ", type" + type + ", class" + sourceClass.getName(), e);
        }
    }

    @Override
    public Class<? extends Source> getClass(String type) {
        String sourceClasName = type;
        SourceType srcType = SourceType.OTHER;

        try {
            srcType = SourceType.valueOf(sourceClasName);
        } catch (IllegalArgumentException ex) {
            log.info("source type {} is a custom type", type);
        }

        if (!srcType.equals(SourceType.OTHER))
            sourceClasName = srcType.getSourceClassName();

        try {
            return (Class<? extends Source>) Class.forName(sourceClasName);
        } catch (Exception e) {
            throw new CannalSinkException("Unable to load source type: " + type, e);
        }

    }
}
