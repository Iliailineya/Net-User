package org.example.client.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ChatClientHandler extends SimpleChannelInboundHandler<String> {

	// Выводит сообщение полученное от сервера
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) {
		System.out.println(msg);
	}
}
