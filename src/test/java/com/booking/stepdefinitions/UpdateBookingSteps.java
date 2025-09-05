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
import static org.junit.Assert.fail;
import java.util.Map;
import java.util.List;

public class UpdateBookingSteps {
    private static final Logger logger = LoggerUtil.getLogger(UpdateBookingSteps.class);
    private final TestContext testContext = TestContext.getInstance();

    @When("I update the booking with the following details:")
    public void iUpdateTheBookingWithTheFollowingDetails(DataTable dataTable) {
        List<Map<String, String>> bookingData = dataTable.asMaps(String.class, String.class);
        Map<String, String> booking = bookingData.get(0);

        BookingDates dates = BookingDates.builder()
            .checkin(booking.get("checkin"))
            .checkout(booking.get("checkout"))
            .build();

        BookingRequest request = BookingRequest.builder()
            .roomid(Integer.parseInt(booking.get("roomid")))
            .firstname(booking.get("firstname"))
            .lastname(booking.get("lastname"))
            .depositpaid(Boolean.parseBoolean(booking.get("depositpaid")))
            .email(booking.get("email"))
            .phone(booking.get("phone"))
            .bookingdates(dates)
            .build();

        SerenityRest
            .given()
            .cookie("token", testContext.getAuthToken())
            .contentType("application/json")
            .body(request)
            .when()
            .put(BookingEndpoint.UPDATE_BOOKING.getUrl(), testContext.getLastBookingId());

        testContext.setLastRequest(request);
        logger.debug("Update booking request sent");
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
