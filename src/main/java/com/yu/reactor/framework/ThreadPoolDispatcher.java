package com.yu.reactor.framework;

import java.nio.channels.SelectionKey;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yuhangbin
 * @date 2021/12/5
 **/
public class ThreadPoolDispatcher implements Dispatcher{

    private ThreadPoolExecutor executor;

    public ThreadPoolDispatcher(int nThread) {
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(nThread);
    }

    @Override
    public void handleEvent(AbstractNioChannel channel, Object clientMessage, SelectionKey key) {
        executor.execute(() -> {
            channel.getEventHandler().handleEvent(channel, clientMessage, key);
        });
    }



    @Override
    public void stop() {
        try {
            executor.shutdown();
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
