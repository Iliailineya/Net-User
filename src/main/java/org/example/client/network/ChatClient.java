package org.example.client.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.example.util.Constants;

import java.util.Scanner;

// Конфигурация клиента
public class ChatClient {

    static String input;
    static Channel channel;

    public void start(Scanner scanner, String userName) throws Exception {

        // Поскольку это клиент, не нужна boss групп.
        // Создаем одну группу EventLoopGroup.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bst = new Bootstrap();
            bst.group(group) // Установка EventLoopGroup, чтобы обрабатывать все события для клиента.
                    .channel(NioSocketChannel.class) // Использование NIO, принять новое соединение
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline pln = ch.pipeline();
                            // Коммуникация сокет-канал происходит в потоках байтов.
                            // Декодер строк и кодировщик помогают преобразованию
                            // между байтами и строкой.
                            pln.addLast(new StringDecoder());
                            pln.addLast(new StringEncoder());

                            // Клиентский обработчик.
                            pln.addLast(new ChatClientHandler());
                        }
                    });

            // Старт клиента.
            ChannelFuture ft = bst.connect(Constants.HOST,
                    Constants.PORT).sync();

            // Цикл получения входных сообщений чата от пользователя,
            // а затем отправка на сервер.
            while (scanner.hasNext()) {
                input = scanner.nextLine();
                if (input.equals("quit")) System.exit(0);
                channel = ft.sync().channel();
                channel.writeAndFlush("[" + userName + "]: " + input);
                channel.flush();
            }

            // Ожидание пока соединение не будет закрыто.
            ft.channel().closeFuture().sync();
        } finally {
            // Завершение всех циклов обработки событий,
            // чтобы завершить все потоки.
            group.shutdownGracefully();
        }
    }
}
