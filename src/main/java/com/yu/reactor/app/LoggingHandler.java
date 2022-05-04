package com.yu.reactor.app;

import com.yu.reactor.framework.AbstractNioChannel;
import com.yu.reactor.framework.EventHandler;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.charset.StandardCharsets;

/**
 * @author yuhangbin
 * @date 2021/12/5
 **/
public class LoggingHandler implements EventHandler {
    public final static byte[] ACK = "success".getBytes(StandardCharsets.UTF_8);

    @Override
    public void handleEvent(AbstractNioChannel channel, Object clientMessage, SelectionKey key) {
        if (clientMessage instanceof ByteBuffer) {
            doLogging(channel, key, (ByteBuffer)clientMessage);
            reply(channel, key);
        }else {
            throw new IllegalStateException("Unknown data received");
        }
    }

    private void doLogging(AbstractNioChannel channel, SelectionKey key, ByteBuffer message) {
        message.flip();
        byte[] clientMessage = new byte[message.limit()];
        message.get(clientMessage);
        System.out.println(new String(clientMessage, StandardCharsets.UTF_8));
    }

    private void reply(AbstractNioChannel channel, SelectionKey key) {
        try {
            System.out.println("reply : " + new String(ACK));
            channel.write(ByteBuffer.wrap(ACK), key);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
