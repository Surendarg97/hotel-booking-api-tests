@authenticationfeature
Feature: Authentication API Endpoints
  As a user of the booking system
  I want to manage authentication
  So that I can access protected endpoints securely

  Background:
    Given the authentication API is available

  @login @smoke
  Scenario Outline: User login with valid credentials
    When I send a login request with username "<username>" and password "<password>"
    Then the response status code should be 200
    And the response should contain a valid token
    And the token should not be empty

    Examples:
      | username | password    |
      | admin    | password |

  @validate @smoke
  Scenario: Validate a valid authentication token
    Given I have a valid authentication token
    When I send a validate token request
    Then the response status code should be 200
    And the token should be validated successfully

  @logout @smoke
  Scenario: Successfully logout with valid token
    Given I have a valid authentication token
    When I send a logout request
    Then the response status code should be 200
    And the token should be invalidated

  @login @negative
  Scenario Outline: User login with invalid credentials
    When I send a login request with username "<username>" and password "<password>"
    Then the response status code should be 401

    Examples:
      | username | password |
      | invalid  | wrong    |
      | admin    | wrong    |
      | invalid  | password |

  @validate @negative
  Scenario: Validate an invalid authentication token
    Given I have an invalid authentication token
    When I send a validate token request
    Then the response status code should be 403

  @logout @negative
  Scenario: Attempt to logout with invalid token
    Given I have an invalid authentication token
    When I send a logout request
    Then the response status code should be 403

  @regression
  Scenario: Complete authentication flow
    Given I have no active authentication token
    When I send a login request with username "admin" and password "password"
    Then the response status code should be 200
    And the response should contain a valid token
    When I send a validate token request
    Then the response status code should be 200
    When I send a logout request
    Then the response status code should be 200
    When I send a validate token request
    Then the response status code should be 403
