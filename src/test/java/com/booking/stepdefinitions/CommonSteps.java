package com.booking.stepdefinitions;

import com.booking.pojo.*;
import com.booking.utils.TestContext;
import com.booking.utils.ApiUtils;
import com.booking.enums.BookingEndpoint;
import com.booking.enums.AuthEndpoint;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.rest.SerenityRest;
import org.junit.Assert;
import org.slf4j.Logger;
import com.booking.utils.LoggerUtil;
import com.booking.utils.ConfigurationUtil;

import java.util.List;

import static net.serenitybdd.rest.SerenityRest.lastResponse;
import static org.junit.Assert.fail;

public class CommonSteps {
    private static final Logger logger = LoggerUtil.getLogger(CommonSteps.class);
    private final TestContext testContext = TestContext.getInstance();

    @Given("I fetch a booking for room {int}")
    @Given("a booking exists is available for room {int}")
    @When("I retrieve the booking details with room id {int}")
    public void iFetchABookingForRoom(int roomId) {
        logger.info("Fetching booking details for room ID: {}", roomId);
        
        // First ensure we have a valid token
        requestAuthenticationToken();
        
        // Get bookings filtered by room ID
        SerenityRest
            .given()
            .cookie("token", testContext.getAuthToken())
            .queryParam("roomid", roomId)
            .when()
            .get(BookingEndpoint.CREATE_BOOKING.getUrl());

        if (SerenityRest.lastResponse().getStatusCode() == 200) {
            // Extract and store the first booking ID
            BookingSummaries bookingSummaries = ApiUtils.mapResponseToPojo(SerenityRest.lastResponse(), BookingSummaries.class);
            if (bookingSummaries != null) {
                testContext.setLastBookingId(bookingSummaries.getBookings().get(0).getBookingid());
                logger.info("Found booking ID: {} for room: {}", bookingSummaries.getBookings().get(0).getBookingid()
                        , bookingSummaries.getBookings().get(0).getRoomid());
            } else {
                logger.error("No booking found for room ID: {}", roomId);
                fail("No booking found for room ID: " + roomId);
            }
        } else {
            logger.error("Failed to fetch booking. Status code: {}", SerenityRest.lastResponse().getStatusCode());
            fail("Failed to fetch booking with status code: " + SerenityRest.lastResponse().getStatusCode());
        }
    }


    @Given("I have an invalid authentication token")
    public void iHaveAnInvalidAuthenticationToken() {
        logger.info("Setting up an invalid authentication token");
        testContext.setAuthToken("invalid-token-12345");
        logger.debug("Set invalid authentication token in context");
    }

    @Given("I have no active authentication token")
    public void iHaveNoActiveAuthenticationToken() {
        logger.info("Ensuring no active authentication token is present");
        testContext.setAuthToken(null);
        logger.debug("Authentication token has been cleared from context");
    }


    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        lastResponse()
                .then()
                .statusCode(expectedStatusCode);
    }

    @When("I send a validate token request")
    public void iSendAValidateTokenRequest() {
        TokenResponse tokenRequest = TokenResponse.builder()
                .token(testContext.getAuthToken())
                .build();

        SerenityRest
                .given()
                .contentType("application/json")
                .body(tokenRequest)
                .when()
                .post(AuthEndpoint.VALIDATE.getUrl());
    }

    @Then("the token should be invalidated")
    public void theTokenShouldBeInvalidated() {
        // Verify the token is invalidated by trying to validate it
        iSendAValidateTokenRequest();
        theResponseStatusCodeShouldBe(403);
    }

    @Given("the booking API is available")
    public void theBookingAPIIsAvailable() {
        String baseUrl = ConfigurationUtil.getProperty("api.booking.base");
        logger.info("Initializing booking API with base URL: {}", baseUrl);
        ApiUtils.initializeApiConfig(baseUrl);
        logger.debug("API base configuration completed");
    }


    @Then("the response should contain the validation error {string}")
    public void theResponseShouldContainTheValidationError(String expectedError) {
        logger.debug("Validation error response: {}", lastResponse().getBody().asString());

        // Map the response to ErrorResponse POJO
        ErrorResponse errorResponse = ApiUtils.mapResponseToPojo(lastResponse(), ErrorResponse.class);
        Assert.assertNotNull("Error response should not be null", errorResponse);
        Assert.assertNotNull("Error list should not be null", errorResponse.getErrors());

        // Split expected errors if multiple
        String[] expectedErrors = expectedError.split(", ");
        List<String> actualErrors = errorResponse.getErrors();

        // Validate each expected error exists in the response
        for (String expected : expectedErrors) {
            Assert.assertTrue(
                    String.format("Expected error message '%s' not found in response errors: %s",
                            expected, String.join(", ", actualErrors)),
                    actualErrors.contains(expected.trim())
            );
        }

        logger.info("Successfully validated all error messages: {}", actualErrors);
    }

    public void requestAuthenticationToken() {
        logger.info("Requesting authentication token");
        
        AuthRequest authRequest = AuthRequest.builder()
                .username(ConfigurationUtil.getProperty("auth.default.username"))
                .password(ConfigurationUtil.getProperty("auth.default.password"))
                .build();

        SerenityRest
            .given()
            .contentType("application/json")
            .body(authRequest)
            .when()
            .post(AuthEndpoint.LOGIN.getUrl());

        if (SerenityRest.lastResponse().getStatusCode() == 200) {
            TokenResponse tokenResponse = ApiUtils.mapResponseToPojo(SerenityRest.lastResponse(), TokenResponse.class);
            if (tokenResponse != null) {
                testContext.setAuthToken(tokenResponse.getToken());
                logger.debug("Authentication successful, token stored in context");
            } else {
                logger.error("Failed to parse authentication response");
                fail("Authentication failed - could not parse response");
            }
        } else {
            logger.error("Authentication failed with status code: {}", SerenityRest.lastResponse().getStatusCode());
            fail("Authentication failed with status code: " + SerenityRest.lastResponse().getStatusCode());
        }
    }
}
