package com.yu.reactor.framework;

import java.nio.channels.SelectionKey;

/**
 * @author yuhangbin
 * @date 2021/10/16
 **/
public interface Dispatcher {

    void handleEvent(AbstractNioChannel channel, Object clientMessage, SelectionKey key);

    void stop();
}
