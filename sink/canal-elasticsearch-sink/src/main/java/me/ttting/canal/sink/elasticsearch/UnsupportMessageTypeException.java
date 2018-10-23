package me.ttting.canal.sink.elasticsearch;

/**
 * Created by jiangtiteng on 2018/10/18
 */
public class UnsupportMessageTypeException extends RuntimeException {

    public UnsupportMessageTypeException(FlatMessage flatMessage) {
        super("UnSupport Message Type " + flatMessage.toString());
    }
}
