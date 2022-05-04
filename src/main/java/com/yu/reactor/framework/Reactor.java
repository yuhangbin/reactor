package com.yu.reactor.framework;

import java.io.IOException;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yuhangbin
 * @date 2021/10/16
 **/
public class Reactor {

    private Dispatcher dispatcher;
    // Synchronous Event De-Multiplexer
    private Selector selector;
    // executor select() and dispatcher event
    private ThreadPoolExecutor bossExecutor = new ThreadPoolExecutor(1, 1, 1, TimeUnit.HOURS, new LinkedBlockingQueue<>(1000));

    private List<AbstractNioChannel> channels = new LinkedList<>();

    public Reactor(Dispatcher dispatcher) throws IOException {
        this.dispatcher = dispatcher;
        this.selector = Selector.open();
    }

    public void start() {
        try {
            eventLoop();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            bossExecutor.shutdown();
            if (!bossExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                bossExecutor.shutdownNow();
            }
            this.dispatcher.stop();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Reactor registerChannel(AbstractNioChannel channel) throws IOException {
        channel.getJavaChannel().configureBlocking(false);
        channel.bind();
        SelectionKey key = channel.getJavaChannel().register(selector, SelectionKey.OP_ACCEPT);
        key.attach(channel);
        channel.setReactor(this);
        this.channels.add(channel);
        return this;
    }

    private void eventLoop() throws IOException {
        while (!Thread.interrupted()) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                var key = iterator.next();
                if (!key.isValid()) {
                    iterator.remove();
                    continue;
                }
                processKey(key);
            }
            keys.clear();
        }
    }

    private void processKey(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            System.out.println("key is acceptable");
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
            SocketChannel clientChannel = serverSocketChannel.accept();
            clientChannel.configureBlocking(false);
            SelectionKey clientKey = clientChannel.register(selector, SelectionKey.OP_READ);
            clientKey.attach(key.attachment());
        }else if (key.isReadable()) {
            System.out.println("key is readable");
            dispatcher.handleEvent((AbstractNioChannel)key.attachment(), ((AbstractNioChannel)key.attachment()).read(key), key);
        }else if (key.isWritable()) {
            System.out.println("key is writable");
        }
    }

}
