package com.booking.enums;

import com.booking.utils.ConfigurationUtil;

/**
 * Enum for Authentication related endpoints
 */
public enum AuthEndpoint {
    LOGIN("/login"),
    VALIDATE("/validate"),
    LOGOUT("/logout");

    private final String path;
    private static final String BASE_URL = ConfigurationUtil.getProperty("api.auth.base");

    AuthEndpoint(String path) {
        this.path = path;
    }

    /**
     * Get the complete URL for the endpoint
     * @return The complete URL including the base path
     */
    public String getUrl() {
        return BASE_URL + path;
    }

    /**
     * Get just the path portion of the endpoint
     * @return The path without the base URL
     */
    public String getPath() {
        return path;
    }
}
