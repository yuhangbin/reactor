package com.yu.reactor.framework;

import java.nio.channels.SelectionKey;

/**
 * To handle events which send by client.
 * @author yuhangbin
 * @date 2021/10/16
 **/
public interface EventHandler {

    void handleEvent(AbstractNioChannel channel,Object clientMessage, SelectionKey selectionKey);
}
