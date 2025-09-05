package com.booking.utils;

import org.slf4j.Logger;
import com.booking.pojo.BookingRequest;

/**
 * Shared context for test execution
 * This class holds data that needs to be shared between step definitions
 */
public class TestContext {
    private static final Logger logger = LoggerUtil.getLogger(TestContext.class);
    private String authToken;
    private BookingRequest lastRequest;
    private int lastBookingId;
    private static TestContext instance;

    private TestContext() {
        // Private constructor for singleton pattern
    }

    public static synchronized TestContext getInstance() {
        if (instance == null) {
            instance = new TestContext();
        }
        return instance;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public BookingRequest getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(BookingRequest lastRequest) {
        this.lastRequest = lastRequest;
        logger.debug("Stored last request: {}", lastRequest);
    }

    public int getLastBookingId() {
        return lastBookingId;
    }

    public void setLastBookingId(int lastBookingId) {
        this.lastBookingId = lastBookingId;
        logger.debug("Stored last booking ID: {}", lastBookingId);
        logger.debug("Auth token updated in test context");
    }

    public void reset() {
        authToken = null;
        logger.debug("Test context reset");
    }
}
