package com.booking.stepdefinitions;

import io.cucumber.java.en.Then;
import net.serenitybdd.rest.SerenityRest;
import org.slf4j.Logger;
import com.booking.utils.LoggerUtil;
import com.booking.utils.ConfigurationUtil;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class BookingSchemaValidationSteps {
    private static final Logger logger = LoggerUtil.getLogger(BookingSchemaValidationSteps.class);

    @Then("the create booking response schema should be valid")
    public void validateCreateBookingResponseSchema() {
        logger.info("Validating create booking response schema");
        try {
            String schemaPath = ConfigurationUtil.getProperty("schema.booking.create.response");
            logger.debug("Using schema path: {}", schemaPath);
            SerenityRest.then()
                    .assertThat()
                    .body(matchesJsonSchemaInClasspath(schemaPath));
            logger.debug("Create booking response schema validation successful");
        } catch (AssertionError e) {
            logger.error("Create booking response schema validation failed: {}", e.getMessage());
            throw e;
        }
    }

    @Then("the get booking response schema should be valid")
    public void validateGetBookingResponseSchema() {
        logger.info("Validating get booking response schema");
        try {
            String schemaPath = ConfigurationUtil.getProperty("schema.booking.get.response");
            logger.debug("Using schema path: {}", schemaPath);
            SerenityRest.then()
                    .assertThat()
                    .body(matchesJsonSchemaInClasspath(schemaPath));
            logger.debug("Get booking response schema validation successful");
        } catch (AssertionError e) {
            logger.error("Get booking response schema validation failed: {}", e.getMessage());
            throw e;
        }
    }

    @Then("the update booking response schema should be valid")
    public void validateUpdateBookingResponseSchema() {
        logger.info("Validating update booking response schema");
        try {
            String schemaPath = ConfigurationUtil.getProperty("schema.booking.update.response");
            logger.debug("Using schema path: {}", schemaPath);
            SerenityRest.then()
                    .assertThat()
                    .body(matchesJsonSchemaInClasspath(schemaPath));
            logger.debug("Update booking response schema validation successful");
        } catch (AssertionError e) {
            logger.error("Update booking response schema validation failed: {}", e.getMessage());
            throw e;
        }
    }

    @Then("the delete booking response schema should be valid")
    public void validateDeleteBookingResponseSchema() {
        logger.info("Validating delete booking response schema");
        try {
            String schemaPath = ConfigurationUtil.getProperty("schema.booking.delete.response");
            logger.debug("Using schema path: {}", schemaPath);
            SerenityRest.then()
                    .assertThat()
                    .body(matchesJsonSchemaInClasspath(schemaPath));
            logger.debug("Delete booking response schema validation successful");
        } catch (AssertionError e) {
            logger.error("Delete booking response schema validation failed: {}", e.getMessage());
            throw e;
        }
    }
}
