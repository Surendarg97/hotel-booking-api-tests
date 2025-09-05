package com.booking.enums;

import com.booking.utils.ConfigurationUtil;

/**
 * Enum for Booking related endpoints
 */
public enum BookingEndpoint {
    CREATE_BOOKING("/booking"),
    GET_BOOKING("/booking/{0}"),
    UPDATE_BOOKING("/booking/{0}"),
    DELETE_BOOKING("/booking/{0}");

    private final String path;
    private static final String BASE_URL = ConfigurationUtil.getProperty("api.base.url");

    BookingEndpoint(String path) {
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
     * Get the URL with a path parameter replaced
     * @param paramName The parameter name to replace (e.g., "id")
     * @param value The value to replace it with
     * @return The URL with the parameter replaced
     */
    public String getUrl(String paramName, String value) {
        return getUrl().replace("{" + paramName + "}", value);
    }

    /**
     * Get just the path portion of the endpoint
     * @return The path without the base URL
     */
    public String getPath() {
        return path;
    }
}
