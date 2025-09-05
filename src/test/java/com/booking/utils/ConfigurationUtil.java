package com.booking.utils;

import java.io.IOException;
import java.util.Properties;

public class ConfigurationUtil {
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "config.properties";

    static {
        loadProperties();
    }

    private ConfigurationUtil() {
        // Private constructor to prevent instantiation
    }

    private static void loadProperties() {
        try (var inputStream = ConfigurationUtil.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                throw new IOException("Unable to find " + CONFIG_FILE);
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property not found: " + key);
        }
        return value;
    }

    /**
     * Check if a property exists
     * @param key The property key
     * @return true if the property exists, false otherwise
     */
    public static boolean hasProperty(String key) {
        return properties.containsKey(key);
    }
}
