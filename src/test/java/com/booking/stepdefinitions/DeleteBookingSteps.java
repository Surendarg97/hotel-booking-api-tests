package com.booking.stepdefinitions;

import com.booking.utils.TestContext;
import com.booking.enums.BookingEndpoint;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.rest.SerenityRest;
import org.slf4j.Logger;
import com.booking.utils.LoggerUtil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DeleteBookingSteps {
    private static final Logger logger = LoggerUtil.getLogger(DeleteBookingSteps.class);
    private final TestContext testContext = TestContext.getInstance();

    @When("I delete the booking")
    public void iDeleteTheBooking() {
        int bookingId = testContext.getLastBookingId();
        logger.info("Deleting booking with ID: {}", bookingId);

        SerenityRest
            .given()
            .cookie("token", testContext.getAuthToken())
            .when()
            .delete(BookingEndpoint.DELETE_BOOKING.getUrl(), bookingId);

        logger.debug("Delete booking request sent");
    }

    @Then("the booking should be deleted successfully")
    public void theBookingShouldBeDeletedSuccessfully() {
        if (SerenityRest.lastResponse().getStatusCode() == 200) {
            logger.info("Booking deleted successfully");
            
            // Verify the booking no longer exists
            int bookingId = testContext.getLastBookingId();
            SerenityRest
                .given()
                .cookie("token", testContext.getAuthToken())
                .when()
                .get(BookingEndpoint.GET_BOOKING.getUrl(), bookingId);
                
            assertEquals("Booking should not exist after deletion", 405, SerenityRest.lastResponse().getStatusCode());
            logger.debug("Verified booking no longer exists");
        } else {
            logger.error("Failed to delete booking. Status code: {}", SerenityRest.lastResponse().getStatusCode());
            fail("Delete booking request failed with status code: " + SerenityRest.lastResponse().getStatusCode());
        }
    }
    
    @Then("the booking should still exist")
    public void theBookingShouldStillExist() {
        logger.info("Verifying booking still exists after failed deletion attempt");

        testContext.setAuthToken(null);  // Clear the invalid token
        AuthenticationSteps authSteps = new AuthenticationSteps();
        authSteps.iHaveAValidAuthenticationToken();

        int bookingId = testContext.getLastBookingId();
        SerenityRest
            .given()
            .cookie("token", testContext.getAuthToken())
            .when()
            .get(BookingEndpoint.GET_BOOKING.getUrl(), bookingId);
            
        assertEquals("Booking should still exist", 200, SerenityRest.lastResponse().getStatusCode());
        logger.debug("Verified booking still exists after failed deletion attempt");
    }
}
