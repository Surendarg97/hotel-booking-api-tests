package com.booking.utils;

import io.restassured.response.Response;
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

    /**
     * Maps the API response to the specified POJO class with error handling
     * @param response REST-assured Response object
     * @param pojoClass Class to map the response to
     * @return Mapped POJO object or null if mapping fails
     */
    public static <T> T mapResponseToPojo(Response response, Class<T> pojoClass) {
        try {
            String responseBody = response.getBody().asString();
            logger.debug("Mapping response to {}: {}", pojoClass.getSimpleName(), responseBody);

            T mappedObject = response.as(pojoClass);
            logger.debug("Successfully mapped response to {}", pojoClass.getSimpleName());
            return mappedObject;
        } catch (Exception e) {
            logger.error("Failed to map response to {}: {}", pojoClass.getSimpleName(), e.getMessage());
            logger.error("Response body: {}", response.getBody().asString());
            return null;
        }
    }
    
}
