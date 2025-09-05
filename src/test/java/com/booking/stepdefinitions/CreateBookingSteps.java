package com.booking.stepdefinitions;

import com.booking.pojo.BookingDates;
import com.booking.pojo.BookingRequest;
import com.booking.pojo.BookingResponse;
import com.booking.utils.TestContext;
import com.booking.enums.BookingEndpoint;
import com.booking.utils.ApiUtils;
import com.booking.utils.ConfigurationUtil;
import com.booking.utils.LoggerUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;
import net.serenitybdd.rest.SerenityRest;
import org.slf4j.Logger;
import static net.serenitybdd.rest.SerenityRest.lastResponse;
import org.junit.Assert;
import java.util.Map;
import java.util.List;

public class CreateBookingSteps {
    private static final Logger logger = LoggerUtil.getLogger(CreateBookingSteps.class);
    private final TestContext testContext = TestContext.getInstance();

    @Given("the booking API is available")
    public void theBookingAPIIsAvailable() {
        String baseUrl = ConfigurationUtil.getProperty("api.booking.base");
        logger.info("Initializing booking API with base URL: {}", baseUrl);
        ApiUtils.initializeApiConfig(baseUrl);
        logger.debug("API base configuration completed");
    }

    @When("I create a booking with the following details:")
    public void iCreateABookingWithTheFollowingDetails(DataTable dataTable) {
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
            .email(booking.get("email"))
            .phone(booking.get("phone"))
            .depositpaid(Boolean.parseBoolean(booking.get("depositpaid")))
            .bookingdates(dates)
            .build();

        logger.info("Creating booking for {} {}", request.getFirstname(), request.getLastname());
        testContext.setLastRequest(request);
        
        SerenityRest
            .given()
            .contentType("application/json")
            .body(request)
            .when()
            .post(BookingEndpoint.CREATE_BOOKING.getUrl());
    }

    @Then("the booking should be created successfully")
    public void theBookingShouldBeCreatedSuccessfully() {
        BookingResponse response = ApiUtils.mapResponseToPojo(lastResponse(), BookingResponse.class);
        Assert.assertNotNull("Booking response should not be null", response);
        Assert.assertNotNull("Booking details should not be null", response.getBooking());
        
        BookingRequest booking = response.getBooking();
        Assert.assertEquals("First name should match", testContext.getLastRequest().getFirstname(), booking.getFirstname());
        Assert.assertEquals("Last name should match", testContext.getLastRequest().getLastname(), booking.getLastname());
        Assert.assertNotNull("Booking dates should not be null", booking.getBookingdates());
        
        logger.info("Booking created successfully with details: {}", booking);
    }

    @Then("the response should contain the booking ID")
    public void theResponseShouldContainTheBookingId() {
        BookingResponse response = ApiUtils.mapResponseToPojo(lastResponse(), BookingResponse.class);
        Assert.assertNotNull("Booking ID should not be null", response);
        Assert.assertTrue("Booking ID should be greater than 0", response.getBookingid() > 0);
        
        testContext.setLastBookingId(response.getBookingid());
        logger.info("Created booking with ID: {}", response.getBookingid());
    }
}
