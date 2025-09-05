package com.booking.stepdefinitions;

import com.booking.pojo.BookingSummaries;
import com.booking.pojo.UnavailableRoom;
import com.booking.utils.TestContext;
import com.booking.pojo.BookingRequest;
import com.booking.pojo.BookingDates;
import com.booking.utils.ApiUtils;
import com.booking.enums.BookingEndpoint;
import io.cucumber.java.en.Given;
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

import java.util.Collections;
import java.util.Map;
import java.util.List;

public class GetBookingSteps {
    private static final Logger logger = LoggerUtil.getLogger(GetBookingSteps.class);
    private final TestContext testContext = TestContext.getInstance();

    @Given("a booking exists with the following details:")
    public void aBookingExistsWithTheFollowingDetails(DataTable dataTable) {
        List<Map<String, String>> bookingData = dataTable.asMaps(String.class, String.class);
        Map<String, String> booking = bookingData.get(0);

        BookingDates dates = BookingDates.builder()
            .checkin(booking.get("checkin"))
            .checkout(booking.get("checkout"))
            .build();

        BookingRequest request = BookingRequest.builder()
            .roomid(Integer.parseInt(booking.get("roomid")))
            .bookingid(Integer.parseInt(booking.get("bookingid")))
            .firstname(booking.get("firstname"))
            .lastname(booking.get("lastname"))
            .depositpaid(Boolean.parseBoolean(booking.get("depositpaid")))
            .email(booking.get("email"))
            .phone(booking.get("phone"))
            .bookingdates(dates)
            .build();

        // Create the booking first
        SerenityRest
            .given()
            .contentType("application/json")
            .body(request)
            .when()
            .post(BookingEndpoint.CREATE_BOOKING.getUrl());
    }

    @When("I retrieve the booking details with booking id")
    public void iRetrieveTheBookingDetails() {
        int bookingId = testContext.getLastBookingId();
        logger.info("Retrieving booking details for ID: {}", bookingId);

        SerenityRest
            .given()
            .cookie("token", testContext.getAuthToken())
            .when()
            .get(BookingEndpoint.GET_BOOKING.getUrl(), bookingId);

        // Validate response schema by attempting to map to POJO
        if (SerenityRest.lastResponse().getStatusCode() == 200) {
            BookingRequest response = ApiUtils.mapResponseToPojo(SerenityRest.lastResponse(), BookingRequest.class);
            assertNotNull("Response schema validation failed - unable to map to BookingRequest", response);
            logger.debug("Response schema validation successful");
        }

        logger.debug("Get booking request sent");
    }

    @Then("the response should contain the correct booking information of room id {int}")
    public void theResponseShouldContainTheCorrectBookingInformation(int roomId) {
        if (SerenityRest.lastResponse().getStatusCode() == 200) {

            BookingSummaries bookingSummaries = ApiUtils.mapResponseToPojo(SerenityRest.lastResponse(), BookingSummaries.class);
            if (bookingSummaries != null) {
                assertEquals("Room ID should match", roomId, bookingSummaries.getBookings().get(0).getRoomid());
            }
            logger.debug("Booking details validated successfully");

        } else {
            logger.error("Failed to get booking details. Status code: {}", SerenityRest.lastResponse().getStatusCode());
            fail("Get booking request failed with status code: " + SerenityRest.lastResponse().getStatusCode());
        }
    }

    @When("I retrieve the booking summary for room {int}")
    public void iRetrieveTheBookingSummaryForRoom(int roomId) {
        logger.info("Retrieving booking summary for room ID: {}", roomId);
        SerenityRest
            .given()
            .cookie("token", testContext.getAuthToken())
            .queryParam("roomid", roomId)
            .when()
            .get(BookingEndpoint.GET_BOOKING_SUMMARY.getUrl());
    }

    @When("I check for unavailable dates between {string} and {string}")
    public void iCheckForUnavailableDatesBetween(String checkin, String checkout) {
        logger.info("Checking unavailable dates between {} and {}", checkin, checkout);
        SerenityRest
            .given()
            .cookie("token", testContext.getAuthToken())
            .queryParam("checkin", checkin)
            .queryParam("checkout", checkout)
            .when()
            .get(BookingEndpoint.GET_UNAVAILABLE_DATES.getUrl());
    }

    @Then("the response should contain the room booking summary")
    public void theResponseShouldContainTheRoomBookingSummary() {
        if (SerenityRest.lastResponse().getStatusCode() == 200) {
            BookingSummaries summary = ApiUtils.mapResponseToPojo(SerenityRest.lastResponse(), BookingSummaries.class);
            assertNotNull("Response summary should not be null", summary);
            assertNotNull("Bookings list should not be null", summary.getBookings());
            logger.debug("Successfully validated booking summary response with {} bookings", summary.getBookings().size());
        } else {
            logger.error("Failed to get booking summary. Status code: {}", SerenityRest.lastResponse().getStatusCode());
            fail("Get booking summary request failed with status code: " + SerenityRest.lastResponse().getStatusCode());
        }
    }

    @Then("the response should contain the list of unavailable dates")
    public void theResponseShouldContainTheListOfUnavailableDates() {
        if (SerenityRest.lastResponse().getStatusCode() == 200) {
            List<UnavailableRoom> unavailableRooms = Collections.singletonList
                    (ApiUtils.mapResponseToPojo(SerenityRest.lastResponse(), UnavailableRoom.class));
            assertNotNull("Response unavailable rooms list should not be null", unavailableRooms);
        } else {
            logger.error("Failed to get unavailable dates. Status code: {}", SerenityRest.lastResponse().getStatusCode());
            fail("Get unavailable dates request failed with status code: " + SerenityRest.lastResponse().getStatusCode());
        }
    }

    @Then("the response should contain booking not found message")
    public void theResponseShouldContainBookingNotFoundMessage() {
        String responseBody = SerenityRest.lastResponse().getBody().asString();
        logger.debug("Response body for not found booking: {}", responseBody);
        
        if (SerenityRest.lastResponse().getStatusCode() == 404) {
            // Verify the error message indicates booking not found
            assertTrue("Response should contain 'not found' message", 
                responseBody.toLowerCase().contains("not found") || 
                responseBody.toLowerCase().contains("no booking"));
            logger.info("Successfully validated booking not found message");
        } else {
            logger.error("Expected 404 status code but got: {}", SerenityRest.lastResponse().getStatusCode());
            fail("Expected 404 status code for invalid room id");
        }
    }
}
