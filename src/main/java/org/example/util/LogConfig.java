package org.example.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;

public class LogConfig {
    public static void configureLogging(String name) {
        try {
            FileInputStream configFile = new FileInputStream(name + "logs.properties");
            LogManager.getLogManager().readConfiguration(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}