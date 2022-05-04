package com.yu.reactor.app;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author yuhangbin
 * @date 2021/12/5
 **/
public class AppClient {

	public static void main(String[] args) throws Exception{
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress("localhost", 60000));
		String log = "hello I'm client. And logging something to server.";
		socketChannel.write(ByteBuffer.wrap(log.getBytes(StandardCharsets.UTF_8)));
		// receive server response
		ByteBuffer buffer = ByteBuffer.allocate(128);
		socketChannel.read(buffer);
		buffer.flip();
		byte[] serverResponse = new byte[buffer.limit()];
		buffer.get(serverResponse);
		System.out.println("server response: " + new String(serverResponse, StandardCharsets.UTF_8));
		socketChannel.close();
	}
}
