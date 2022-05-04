package com.yu.reactor.app;

import com.yu.reactor.framework.Dispatcher;
import com.yu.reactor.framework.Reactor;
import com.yu.reactor.framework.ServerNioChannel;
import com.yu.reactor.framework.ThreadPoolDispatcher;

/**
 * @author yuhangbin
 * @date 2021/12/5
 **/
public class App {


    public static void main(String[] args) {
        try {
            Dispatcher dispatcher = new ThreadPoolDispatcher(10);
//            dispatcher.registerHandler(new LoggingHandler());
            Reactor reactor = new Reactor(dispatcher);
            reactor.registerChannel(new ServerNioChannel(60000, new LoggingHandler()))
                    .start();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
