package com.booking.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for logging throughout the test framework
 */
public class LoggerUtil {
    
    private LoggerUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Get a logger instance for the specified class
     * @param clazz The class to get the logger for
     * @return Logger instance
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
