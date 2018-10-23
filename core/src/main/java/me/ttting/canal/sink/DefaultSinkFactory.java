package me.ttting.canal.sink;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import me.ttting.canal.*;


/**
 * Created by jiangtiteng on 2018/10/21
 */
@Slf4j
public class DefaultSinkFactory implements SinkFactory {
    @Override
    public Sink create(String sinkName, String type) {
        Preconditions.checkNotNull(sinkName, "sourceName");
        Preconditions.checkNotNull(type, "type");

        Class<? extends Sink> sinkClass = getClass(type.toUpperCase());
        try {
            Sink sink = sinkClass.newInstance();
            sink.setName(sinkName);
            return sink;
        } catch (Exception e) {
            throw new CannalSinkException("Unable to create sink: " + sinkName + ", type: " + type, e);
        }
    }

    @Override
    public Class<? extends Sink> getClass(String type) {
        String sinkClassName = type;
        SinkType sinkType = SinkType.OTHER;

        try {
            sinkType = SinkType.valueOf(type);
        } catch (IllegalArgumentException ex) {
            log.info("sink type {} is a custom type ", type);
        }

        if (!sinkType.equals(SinkType.OTHER))
            sinkClassName = sinkType.getSinkClassName();

        try {
            return (Class<? extends Sink>) Class.forName(sinkClassName);
        } catch (Exception e) {
            throw new CannalSinkException("Unableto load sink type : " +type, e);
        }

    }
}
