package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Logger;

import static org.example.util.Constants.DATABASE_URL;

public class SQLiteDB {
    private static final Logger logger = Logger.getLogger(SQLiteDB.class.getName());
    private static Connection connection;
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Если соединение еще не создано или закрыто, создаем новое соединение.
                // Указываем путь к файлу базы данных SQLite (users.db)
                connection = DriverManager.getConnection(DATABASE_URL);
            }
        } catch (SQLException e) {
            logger.warning(e.getMessage() + Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        return connection;
    }
}