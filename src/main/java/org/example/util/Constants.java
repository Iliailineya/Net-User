package org.example.util;

import java.text.SimpleDateFormat;

public final class Constants {
    public static final String DATABASE_URL = "jdbc:sqlite:src/main/resources/users.db";
    public static final int PORT = 8001;
    public static final String HOST = "127.0.0.1";
    public static final String SERVER_START_MSG = "Server started and waiting for clients...";
    public static final String CLOSING_MSG = "Closing connection for client - ";
    public static final String CLIENT_CLOSE_MSG = "quit";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");



    private Constants() {
        // Забороняємо створення екземплярів цього класу.
    }
}
