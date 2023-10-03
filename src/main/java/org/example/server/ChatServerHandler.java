package org.example.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.client.Client;
import org.example.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static org.example.util.Constants.*;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger logger = Logger.getLogger(Client.class.getName());
    private static final List<Channel> channels = new ArrayList<>();

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        System.out.println("Client joined - " + ctx);
        logger.info("Client joined - " + ctx);
        channels.add(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        if (msg.equals(CLIENT_CLOSE_MSG)) {
            System.out.println(CLOSING_MSG + ctx);
            for (Channel ch : channels) {
                ch.writeAndFlush(msg);
            }
            ctx.close();
        }
        if (msg.startsWith("/login ")) {
            String[] parts = msg.split(" ");
            if (parts.length == 3) {
                String username = parts[1];
                String password = parts[2];
                if (DatabaseHandler.isPasswordCorrect(username, password)) {
                    // Аутентификация прошла успешно, разрешаем клиенту отправлять сообщения.
                    // Добавьте клиента в список активных клиентов или выполните другие действия.
                    // ...
                    ctx.writeAndFlush("Authentication successful. Welcome, " + username + "!");
                } else {
                    // Неправильный логин или пароль, отправьте сообщение клиенту об ошибке.
                    ctx.writeAndFlush("Authentication failed. Please check your username and password.");
                    logger.warning("Authentication failed.");
                }
            }
        } else if (msg.startsWith("/register ")) {
            String[] parts = msg.split(" ");
            if (parts.length == 3) {
                String username = parts[1];
                String password = parts[2];
                if (DatabaseHandler.registerUser(username, password)) {
                    ctx.writeAndFlush("Registration successful. You can now log in.");
                } else {
                    ctx.writeAndFlush("Registration failed. Please try again.");
                    logger.warning("Registration failed. Please try again.");
                }
            }
        } else {
            // Обработка сообщений чата после аутентификации.
            String time = DATE_FORMAT.format(new Date());
            System.out.println("CLIENT - ( " + time + " ) " + msg);
            for (Channel ch : channels) {
                ch.writeAndFlush("( " + time + " ) " + msg);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println(CLOSING_MSG + ctx);
        logger.warning(CLOSING_MSG + ctx);
        ctx.close();
    }
}
