package com.booking.stepdefinitions;

import com.booking.pojo.*;
import com.booking.utils.TestContext;
import com.booking.utils.ApiUtils;
import com.booking.enums.BookingEndpoint;
import com.booking.enums.AuthEndpoint;
import io.cucumber.java.en.Given;
import net.serenitybdd.rest.SerenityRest;
import org.slf4j.Logger;
import com.booking.utils.LoggerUtil;
import com.booking.utils.ConfigurationUtil;
import static org.junit.Assert.fail;

public class CommonSteps {
    private static final Logger logger = LoggerUtil.getLogger(CommonSteps.class);
    private final TestContext testContext = TestContext.getInstance();

    @Given("I fetch a booking for room {int}")
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
