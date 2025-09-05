    package com.booking.hooks;
    
    import com.booking.utils.ApiUtils;
    import com.booking.utils.ConfigurationUtil;
    import com.booking.utils.LoggerUtil;
    import com.booking.utils.TestContext;
    import io.cucumber.java.After;
    import io.cucumber.java.AfterAll;
    import io.cucumber.java.Before;
    import io.cucumber.java.BeforeAll;
    import net.serenitybdd.rest.SerenityRest;
    import org.slf4j.Logger;
    
    /**
     * Cucumber hooks for test lifecycle management
     */
    public class Hooks {
        private static final Logger logger = LoggerUtil.getLogger(Hooks.class);
        private final TestContext testContext = TestContext.getInstance();
    
        @BeforeAll
        public static void beforeAllTests() {
            logger.info("========== Test Suite Execution Started ==========");
            
            // Initialize core configurations
            try {
                // Load and validate essential configurations
                String baseUrl = ConfigurationUtil.getProperty("api.base.url");
                String authBaseUrl = ConfigurationUtil.getProperty("api.auth.base");
                String bookingBaseUrl = ConfigurationUtil.getProperty("api.booking.base");
                
                // Validate auth credentials exist
                ConfigurationUtil.getProperty("auth.default.username");
                ConfigurationUtil.getProperty("auth.default.password");
                
                logger.info("Configuration validation successful");
                logger.info("Base URL: {}", baseUrl);
                logger.info("Auth Base URL: {}", authBaseUrl);
                logger.info("Booking Base URL: {}", bookingBaseUrl);
    
                // Initialize API configuration
                ApiUtils.initializeApiConfig(baseUrl);

                logger.info("Test environment initialization completed successfully");
            } catch (Exception e) {
                logger.error("Failed to initialize test environment: {}", e.getMessage());
                throw new RuntimeException("Test environment initialization failed", e);
            }
        }
    
        @Before
        public void beforeEachScenario() {
            logger.info("------------- Starting New Scenario -------------");
            // Reset REST Assured/Serenity state before each scenario
            SerenityRest.reset();
            // Reset TestContext for new scenario
            testContext.reset();
        }
    
        @After
        public void afterEachScenario() {
            // Clean up scenario-specific resources
            SerenityRest.reset();
            testContext.reset();
            logger.info("------------- Scenario Completed -------------");
        }
    
        @AfterAll
        public static void afterAllTests() {
            try {
                // Clean up any open resources
                SerenityRest.reset();

                logger.info("========== Test Suite Execution Completed ==========");
            } catch (Exception e) {
                logger.error("Error during test suite cleanup: {}", e.getMessage());
            }
        }
    }
