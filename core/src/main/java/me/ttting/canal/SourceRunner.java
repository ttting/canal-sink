package me.ttting.canal;

import me.ttting.canal.lifecycle.LifecycleAware;

/**
 * Created by jiangtiteng on 2018/10/9
 */
public abstract class SourceRunner implements LifecycleAware {
    private Source source;

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
