package com.example.proburok;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final String CONFIG_FILE = "/app1.properties";
    private static Properties properties = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IOException("Файл конфигурации не найден: " + CONFIG_FILE);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке конфигурации", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
