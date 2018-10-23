package me.ttting.canal.event;

import java.util.Map;

/**
 * Created by jiangtiteng on 2018/10/17
 */
public interface Event {
    Map<String, String> getHeaders();

    void setHeaders(Map<String, String> headers);

    byte[] getBody();

    void setBody(byte[] body);
}
