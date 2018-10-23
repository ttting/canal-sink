package me.ttting.canal.event;

import java.util.Map;

/**
 * Created by jiangtiteng on 2018/10/17
 */
public class SimpleEvent implements Event{
    private Map<String, String> headers;

    private byte[] body;

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public byte[] getBody() {
        return body;
    }

    @Override
    public void setBody(byte[] body) {
        this.body = body;
    }
}
