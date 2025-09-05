package com.booking.stepdefinitions;

import io.cucumber.java.en.Then;
import net.serenitybdd.rest.SerenityRest;
import org.slf4j.Logger;
import com.booking.utils.LoggerUtil;
import com.booking.utils.ConfigurationUtil;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class AuthSchemaValidationSteps {
    private static final Logger logger = LoggerUtil.getLogger(AuthSchemaValidationSteps.class);

    @Then("the login response schema should be valid")
    public void validateLoginResponseSchema() {
        logger.info("Validating login response schema");
        try {
            String schemaPath = ConfigurationUtil.getProperty("schema.login.response");
            logger.debug("Using schema path: {}", schemaPath);
            SerenityRest.then()
                    .assertThat()
                    .body(matchesJsonSchemaInClasspath(schemaPath));
            logger.debug("Login response schema validation successful");
        } catch (AssertionError e) {
            logger.error("Login response schema validation failed: {}", e.getMessage());
            throw e;
        }
    }

    @Then("the token validation response schema should be valid")
    public void validateTokenValidationResponseSchema() {
        logger.info("Validating token validation response schema");
        try {
            String schemaPath = ConfigurationUtil.getProperty("schema.validate.response");
            logger.debug("Using schema path: {}", schemaPath);
            SerenityRest.then()
                    .assertThat()
                    .body(matchesJsonSchemaInClasspath(schemaPath));
            logger.debug("Token validation response schema validation successful");
        } catch (AssertionError e) {
            logger.error("Token validation response schema validation failed: {}", e.getMessage());
            throw e;
        }
    }

    @Then("the logout response schema should be valid")
    public void validateLogoutResponseSchema() {
        logger.info("Validating logout response schema");
        try {
            String schemaPath = ConfigurationUtil.getProperty("schema.logout.response");
            logger.debug("Using schema path: {}", schemaPath);
            SerenityRest.then()
                    .assertThat()
                    .body(matchesJsonSchemaInClasspath(schemaPath));
            logger.debug("Logout response schema validation successful");
        } catch (AssertionError e) {
            logger.error("Logout response schema validation failed: {}", e.getMessage());
            throw e;
        }
    }
}
