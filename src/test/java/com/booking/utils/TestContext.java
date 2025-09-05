package com.booking.utils;

import org.slf4j.Logger;

/**
 * Shared context for test execution
 * This class holds data that needs to be shared between step definitions
 */
public class TestContext {
    private static final Logger logger = LoggerUtil.getLogger(TestContext.class);
    private String authToken;
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
        logger.debug("Auth token updated in test context");
    }

    public void reset() {
        authToken = null;
        logger.debug("Test context reset");
    }
}
