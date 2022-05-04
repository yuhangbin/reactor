package com.yu.reactor.framework;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author yuhangbin
 * @date 2021/12/5
 **/
public class ServerNioChannel extends AbstractNioChannel{

    private final int port;

    public ServerNioChannel(int port, EventHandler handler) throws IOException {
        super(ServerSocketChannel.open(), handler);
        this.port = port;
    }

    public ServerSocketChannel getJavaChannel() {
        return (ServerSocketChannel) getChannel();
    }

    @Override
    public int getInterestOps() {
        return SelectionKey.OP_ACCEPT;
    }

    @Override
    public ByteBuffer read(SelectionKey key) throws IOException {
        SocketChannel clientSocket = (SocketChannel) key.channel();
        ByteBuffer message = ByteBuffer.allocate(1024);
        // todo if buffer capacity not enough
        int read = clientSocket.read(message);
        if (read == -1) {
            throw new IOException("Socket closed");
        }
        return message;
    }

    @Override
    public void write(ByteBuffer buffer, SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        clientChannel.write(buffer);
    }

    @Override
    public void bind() throws IOException {
        System.out.println("server listening on port: " + port);
        getJavaChannel().socket().bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
    }
}
