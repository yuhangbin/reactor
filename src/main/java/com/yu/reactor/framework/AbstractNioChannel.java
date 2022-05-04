package com.yu.reactor.framework;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

/**
 * @author yuhangbin
 * @date 2021/12/5
 **/
@Data
@NoArgsConstructor
public abstract class AbstractNioChannel {

    private SelectableChannel channel;
    private EventHandler eventHandler;
    private Reactor reactor;

    public AbstractNioChannel(SelectableChannel channel, EventHandler eventHandler) {
        this.channel = channel;
        this.eventHandler = eventHandler;
    }

    public SelectableChannel getJavaChannel() {
        return this.channel;
    }

    public abstract int getInterestOps();

    public abstract ByteBuffer read(SelectionKey key) throws IOException;

    public abstract void write(ByteBuffer buffer, SelectionKey key) throws IOException;

    public abstract void bind() throws IOException;

}
