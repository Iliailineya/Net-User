package org.example.client;

import org.example.client.network.ChatClient;
import org.example.database.DatabaseHandler;
import org.example.util.LogConfig;

import java.util.Scanner;
import java.util.logging.Logger;

public class Client {
    private static boolean flag = true;
    public static Scanner scanner = new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(Client.class.getName());

    public static void start() {
        LogConfig.configureLogging("client");

        System.out.println("Welcome to the chat application!");
        System.out.print("Do you want to enter the chat? ");
        do {
            System.out.println("Type Y/N: ");
            String option = scanner.nextLine();
            if (option.equalsIgnoreCase("Y")) {
                handleChatEntry(scanner);
                flag = false;
            } else if (option.equalsIgnoreCase("N")) {
                System.out.print("Goodbye.");
                logger.info("Client is quit.");
                flag = false;
            } else {
                System.out.print("Unknown option. Please try again.");
                logger.warning("Unknown option: " + option);
            }
        } while (flag);
    }

    public static void handleChatEntry(Scanner scanner) {
        System.out.print("Please enter your name: ");
        String userName = scanner.nextLine();

        if (DatabaseHandler.isUserRegistered(userName)) {
            handleExistingUser(scanner, userName);
        } else {
            handleNewUser(scanner, userName);
        }
    }

    private static void handleExistingUser(Scanner scanner, String userName) {
        System.out.print("Please enter your password: ");

        do {
            String password = scanner.nextLine();
            if (DatabaseHandler.isPasswordCorrect(userName, password)) {
                System.out.print("Welcome, " + userName + ".\n" +
                        "Type your message to chat or type \"/quit\" to exit.\n");

                ChatClient chatClient = new ChatClient();
                try {
                    chatClient.start(scanner, userName);
                } catch (Exception e) {
                    logger.warning(e.getMessage());
                    throw new RuntimeException(e);
                }
                flag = false;
            } else {
                System.out.print("Incorrect password. Please try again.");
                logger.warning("Incorrect password: " + password);
            }
        } while (flag);
    }

    private static void handleNewUser(Scanner scanner, String userName) {
        logger.info("User not found in the database.");
        System.out.print("Do you want to register? ");
        do {
            System.out.print("Type Y/N: ");
            String option = scanner.nextLine();
            if (option.equalsIgnoreCase("Y")) {
                System.out.print("Please enter a new password: ");
                String newPassword = scanner.nextLine();

                if (DatabaseHandler.registerUser(userName, newPassword)) {
                    System.out.print("Registration successful. You can now log in.");
                    logger.info("Registration successful.");
                    handleExistingUser(scanner, userName);
                    flag = false;
                } else {
                    System.out.print("Registration failed. Please try again.");
                    logger.warning("Registration failed.");
                }
            } else if ((option.equalsIgnoreCase("N"))) {
                System.out.print("Goodbye.");
                flag = false;
            } else {
                System.out.print("Unknown option. Please try again.");
                logger.warning("Unknown option: " + option);
            }
        } while (flag);
    }
}