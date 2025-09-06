package com.booking.stepdefinitions;

import com.booking.utils.TestContext;
import com.booking.pojo.BookingRequest;
import com.booking.pojo.BookingDates;
import com.booking.utils.ApiUtils;
import com.booking.enums.BookingEndpoint;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import net.serenitybdd.rest.SerenityRest;
import org.slf4j.Logger;
import com.booking.utils.LoggerUtil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.Map;
import java.util.List;

public class UpdateBookingSteps {
    private static final Logger logger = LoggerUtil.getLogger(UpdateBookingSteps.class);
    private final TestContext testContext = TestContext.getInstance();


    private void updateBookingWithDetails(int bookingId, DataTable dataTable, boolean includeAllFields) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        Map<String, String> bookingDetails = data.get(0);

        BookingRequest.BookingRequestBuilder requestBuilder = BookingRequest.builder()
                .roomid(Integer.parseInt(bookingDetails.get("roomid")))
                .firstname(bookingDetails.get("firstname"))
                .lastname(bookingDetails.get("lastname"))
                .email(bookingDetails.get("email"))
                .phone(bookingDetails.get("phone"));

        if (includeAllFields) {
            BookingDates dates = BookingDates.builder()
                    .checkin(bookingDetails.get("checkin"))
                    .checkout(bookingDetails.get("checkout"))
                    .build();

            requestBuilder
                    .depositpaid(Boolean.parseBoolean(bookingDetails.get("depositpaid")))
                    .bookingdates(dates);
        }

        BookingRequest request = requestBuilder.build();
        logger.info("Updating booking ID {} with new details", bookingId);

        SerenityRest
                .given()
                .cookie("token", testContext.getAuthToken())
                .contentType("application/json")
                .body(request)
                .when()
                .put(BookingEndpoint.UPDATE_BOOKING.getUrl(), bookingId);

        testContext.setLastRequest(request);
        logger.debug("Update booking request sent");
    }

    @When("I update booking with ID {int} with the following details:")
    public void iUpdateBookingWithIdWithTheFollowingDetails(int bookingId, DataTable dataTable) {
        updateBookingWithDetails(bookingId, dataTable, true);
    }

    @When("I update the booking with the following details:")
    public void iUpdateTheBookingWithTheFollowingDetails(DataTable dataTable) {
        updateBookingWithDetails(testContext.getLastBookingId(), dataTable, true);
    }

    @Then("the booking should be updated successfully")
    public void theBookingShouldBeUpdatedSuccessfully() {
        if (SerenityRest.lastResponse().getStatusCode() == 200) {
            BookingRequest response = ApiUtils.mapResponseToPojo(SerenityRest.lastResponse(), BookingRequest.class);
            BookingRequest request = testContext.getLastRequest();
            
            assertNotNull("Response mapping failed", response);
            logger.info("Successfully mapped response to BookingRequest POJO");
            
            assertEquals("First name should match", request.getFirstname(), response.getFirstname());
            assertEquals("Last name should match", request.getLastname(), response.getLastname());
            assertEquals("Room ID should match", request.getRoomid(), response.getRoomid());
            assertEquals("Email should match", request.getEmail(), response.getEmail());
            assertEquals("Phone should match", request.getPhone(), response.getPhone());
            assertEquals("Deposit paid status should match", request.isDepositpaid(), response.isDepositpaid());
            
            logger.debug("All booking details validated successfully");
        } else {
            logger.error("Failed to update booking. Status code: {}", SerenityRest.lastResponse().getStatusCode());
            fail("Update booking request failed with status code: " + SerenityRest.lastResponse().getStatusCode());
        }
    }

}
