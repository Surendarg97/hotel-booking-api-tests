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

    private BookingRequest buildBookingRequest(Map<String, String> bookingData) {
        BookingDates dates = BookingDates.builder()
            .checkin(bookingData.get("checkin"))
            .checkout(bookingData.get("checkout"))
            .build();

        BookingRequest.BookingRequestBuilder requestBuilder = BookingRequest.builder()
            .roomid(Integer.parseInt(bookingData.get("roomid")))
            .firstname(bookingData.get("firstname"))
            .lastname(bookingData.get("lastname"))
            .email(bookingData.get("email"))
            .phone(bookingData.get("phone"))
            .depositpaid(Boolean.parseBoolean(bookingData.get("depositpaid")))
            .bookingdates(dates);

        if (bookingData.containsKey("bookingid") && bookingData.get("bookingid") != null) {
            requestBuilder.bookingid(Integer.parseInt(bookingData.get("bookingid")));
        }

        return requestBuilder.build();
    }

    @When("I create a booking with the following details:")
    public void iCreateABookingWithTheFollowingDetails(DataTable dataTable) {
        List<Map<String, String>> bookingData = dataTable.asMaps(String.class, String.class);
        Map<String, String> booking = bookingData.get(0);

        BookingRequest request = buildBookingRequest(booking);

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

    @Then("the response should contain the validation error {string}")
    public void theResponseShouldContainTheValidationError(String expectedError) {
        String responseBody = lastResponse().getBody().asString();
        logger.debug("Validation error response: {}", responseBody);
        
        Assert.assertTrue(
            String.format("Response should contain validation error: %s", expectedError),
            responseBody.contains(expectedError)
        );
        logger.info("Successfully validated error message: {}", expectedError);
    }
}
