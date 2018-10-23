package me.ttting.canal.event;

import java.util.Map;

/**
 * Created by jiangtiteng on 2018/10/17
 */
public class EventBuilder {
    public static Event buildEvent(byte[] body, Map<String, String> headers) {
        Event event = new SimpleEvent();

        if (body == null) {
            body = new byte[0];
        }

        if (headers != null) {
            event.setHeaders(headers);
        }
        event.setBody(body);
        return event;
    }
}
