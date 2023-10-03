package org.example.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Logger;

public class DatabaseHandler {

    private static final Logger logger = Logger.getLogger(DatabaseHandler.class.getName());

    // Проверяет, есть ли пользователь в базе данных
    public static boolean isUserRegistered(String userName) {
        try (Connection connection = SQLiteDB.getConnection()) {
            String query = "SELECT * FROM Users WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // Возвращает true, если пользователь существует
        } catch (SQLException e) {
            logger.warning(e.getMessage() + Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            return false; // В случае ошибки также возвращаем false
        }
    }

    // Проверяет, соответствует ли пароль данному пользователю
    public static boolean isPasswordCorrect(String userName, String password) {
        try (Connection connection = SQLiteDB.getConnection()) {
            String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // Возвращает true, если пароль верен
        } catch (SQLException e) {
            logger.warning(e.getMessage() + Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            return false; // В случае ошибки также возвращаем false
        }
    }

    // Регистрирует нового пользователя в базе данных
    public static boolean registerUser(String userName, String password) {
        try (Connection connection = SQLiteDB.getConnection()) {
            String query = "INSERT INTO Users (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1; // Возвращает true, если регистрация успешна
        } catch (SQLException e) {
            logger.warning(e.getMessage() + Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            return false; // В случае ошибки также возвращаем false
        }
    }
}
