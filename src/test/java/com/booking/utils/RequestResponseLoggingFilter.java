package com.booking.utils;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.slf4j.Logger;

/**
 * Custom logging filter for RestAssured to log both request and response details
 */
public class RequestResponseLoggingFilter implements Filter {
    private static final Logger logger = LoggerUtil.getLogger(RequestResponseLoggingFilter.class);

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                         FilterableResponseSpecification responseSpec,
                         FilterContext ctx) {

        logRequest(requestSpec);
        Response response = ctx.next(requestSpec, responseSpec);
        logResponse(response);
        
        return response;
    }

    private void logRequest(FilterableRequestSpecification requestSpec) {
        logger.info("------------------REQUEST------------------");
        logger.info("Request Method: {}", requestSpec.getMethod());
        logger.info("Request URL: {}", requestSpec.getURI());
        
        // Log Headers
        io.restassured.http.Headers headers = requestSpec.getHeaders();
        if (headers != null && headers.asList() != null && !headers.asList().isEmpty()) {
            logger.info("Request Headers:");
            headers.asList().forEach(header -> 
                logger.info("\t{}: {}", header.getName(), header.getValue()));
        }

        // Log Query Parameters
        if (!requestSpec.getQueryParams().isEmpty()) {
            logger.info("Query Parameters:");
            requestSpec.getQueryParams().forEach((name, value) -> 
                logger.info("\t{}: {}", name, value));
        }

        // Log Request Body
        if (requestSpec.getBody() != null) {
            logger.info("Request Body:");
            logger.info(requestSpec.getBody().toString());
        }
        logger.info("-----------------------------------------");
    }

    private void logResponse(Response response) {
        logger.info("------------------RESPONSE-----------------");
        logger.info("Response Status: {} ({})", 
            response.getStatusCode(), response.getStatusLine());
        
        // Log Headers
        io.restassured.http.Headers responseHeaders = response.getHeaders();
        if (responseHeaders != null && responseHeaders.asList() != null && !responseHeaders.asList().isEmpty()) {
            logger.info("Response Headers:");
            responseHeaders.asList().forEach(header ->
                logger.info("\t{}: {}", header.getName(), header.getValue()));
        }

        // Log Response Body
        if (response.getBody() != null) {
            try {
                String responseBody = response.getBody().prettyPrint();
                if (!responseBody.isEmpty()) {
                    logger.info("Response Body:");
                    logger.info(responseBody);
                }
            } catch (Exception e) {
                logger.warn("Could not log response body: {}", e.getMessage());
            }
        }
        logger.info("-----------------------------------------");
    }
}
