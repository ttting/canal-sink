package me.ttting.canal;

import me.ttting.canal.lifecycle.LifecycleAware;

/**
 * Created by jiangtiteng on 2018/10/9
 */
public interface Source extends LifecycleAware, NamedComponent {

    Channel getChannel();

    void setChannel(Channel channel);


}
