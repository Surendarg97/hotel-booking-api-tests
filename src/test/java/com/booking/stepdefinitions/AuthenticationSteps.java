package com.booking.stepdefinitions;

import com.booking.utils.TestContext;
import com.booking.pojo.AuthRequest;
import com.booking.pojo.TokenResponse;
import com.booking.utils.LoggerUtil;
import com.booking.utils.ConfigurationUtil;
import com.booking.utils.ApiUtils;
import com.booking.enums.AuthEndpoint;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.rest.SerenityRest;
import org.junit.Assert;
import org.slf4j.Logger;

import static net.serenitybdd.rest.SerenityRest.lastResponse;
import static org.hamcrest.Matchers.notNullValue;

public class AuthenticationSteps {
    private static final Logger logger = LoggerUtil.getLogger(AuthenticationSteps.class);
    private final TestContext testContext = TestContext.getInstance();

    @Given("the authentication API is available")
    public void theAuthenticationAPIIsAvailable() {
        String baseUrl = ConfigurationUtil.getProperty("api.auth.base");
        logger.info("Initializing authentication API with base URL: {}", baseUrl);
        ApiUtils.initializeApiConfig(baseUrl);
        logger.debug("API base configuration completed");
    }

    @When("I send a login request with username {string} and password {string}")
    public void iSendALoginRequestWithUsernameAndPassword(String username, String password) {
        logger.info("Attempting login for user: {}", username);
        AuthRequest authRequest = AuthRequest.builder()
                .username(username)
                .password(password)
                .build();

        SerenityRest
            .given()
            .contentType("application/json")
            .body(authRequest)
            .when()
            .post(AuthEndpoint.LOGIN.getUrl());

        // Store token if response is successful
        if (lastResponse().getStatusCode() == 200) {
            TokenResponse tokenResponse = ApiUtils.mapResponseToPojo(lastResponse(), TokenResponse.class);
            if (tokenResponse != null) {
                testContext.setAuthToken(tokenResponse.getToken());
                logger.debug("Login successful, token received");
            } else {
                logger.error("Failed to parse login response");
            }
        } else {
            logger.warn("Login failed with status code: {}", lastResponse().getStatusCode());
        }
    }

    @Given("I have a valid authentication token")
    public void iHaveAValidAuthenticationToken() {
        // Login to get a valid token
        iSendALoginRequestWithUsernameAndPassword("admin", "password");
        Assert.assertEquals(200, lastResponse().getStatusCode());
    }

    @When("I send a logout request")
    public void iSendALogoutRequest() {
        TokenResponse tokenRequest = TokenResponse.builder()
                .token(testContext.getAuthToken())
                .build();

        SerenityRest
            .given()
            .contentType("application/json")
            .body(tokenRequest)
            .when()
            .post(AuthEndpoint.LOGOUT.getUrl());
    }

    @Then("the response should contain a valid token")
    public void theResponseShouldContainAValidToken() {
        lastResponse()
            .then()
            .body("token", notNullValue());
        
        TokenResponse tokenResponse = ApiUtils.mapResponseToPojo(lastResponse(), TokenResponse.class);
        Assert.assertNotNull("Failed to parse token response", tokenResponse);
        Assert.assertNotNull("Token should not be null", tokenResponse.getToken());
    }

    @Then("the token should not be empty")
    public void theTokenShouldNotBeEmpty() {
        TokenResponse tokenResponse = ApiUtils.mapResponseToPojo(lastResponse(), TokenResponse.class);
        Assert.assertNotNull("Failed to parse token response", tokenResponse);
        Assert.assertFalse("Token should not be empty", tokenResponse.getToken().isEmpty());
    }

    @Then("the token should be validated successfully")
    public void theTokenShouldBeValidatedSuccessfully() {
        TokenResponse tokenResponse = ApiUtils.mapResponseToPojo(lastResponse(), TokenResponse.class);
        Assert.assertNotNull("Failed to parse validation response", tokenResponse);
        Assert.assertNotNull("Validated token should not be null", tokenResponse.getToken());
    }

}
