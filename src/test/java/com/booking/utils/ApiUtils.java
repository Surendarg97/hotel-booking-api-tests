package com.booking.utils;

import io.restassured.specification.RequestSpecification;
import net.serenitybdd.rest.SerenityRest;
import org.slf4j.Logger;

/**
 * Utility class for API configurations and common specifications with SerenityRest integration
 */
public class ApiUtils {
    private static final Logger logger = LoggerUtil.getLogger(ApiUtils.class);
    private static final RequestResponseLoggingFilter loggingFilter = new RequestResponseLoggingFilter();

    private ApiUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Creates a base request specification with common configurations using SerenityRest
     * @return RequestSpecification with common configurations
     */
    public static RequestSpecification getBaseRequestSpec() {
        return SerenityRest.given()
                .filter(loggingFilter)
                .contentType("application/json");
    }

    /**
     * Initialize SerenityRest configuration with base URL and logging
     * @param baseUrl Base URL for the API
     */
    public static void initializeApiConfig(String baseUrl) {
        logger.info("Initializing SerenityRest configuration with base URL: {}", baseUrl);
        SerenityRest.reset();
        SerenityRest.setDefaultBasePath(baseUrl);
        
        // Enable Serenity's built-in logging
        SerenityRest.enableLoggingOfRequestAndResponseIfValidationFails();
        
        // Add our custom logging filter for additional detail in the log files
        SerenityRest.filters(loggingFilter);
        
        logger.debug("SerenityRest configuration completed");
    }
}
