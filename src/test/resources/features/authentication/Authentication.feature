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
    Then the response status code should be <statusCode>
    And the response should contain a valid token
    And the token should not be empty

    Examples:
      | username | password | statusCode |
      | admin    | password | 200        |

  @validate @smoke
  Scenario Outline: Validate a valid authentication token
    Given I have a valid authentication token
    When I send a validate token request
    Then the response status code should be <statusCode>
    And the token should be validated successfully

    Examples:
      | statusCode |
      | 200       |

  @logout @smoke
  Scenario Outline: Successfully logout with valid token
    Given I have a valid authentication token
    When I send a logout request
    Then the response status code should be <statusCode>
    And the token should be invalidated

    Examples:
      | statusCode |
      | 200       |

  @login @negative
  Scenario Outline: User login with invalid credentials
    When I send a login request with username "<username>" and password "<password>"
    Then the response status code should be <statusCode>

    Examples:
      | username | password | statusCode |
      | invalid  | wrong    | 401        |
      | admin    | wrong    | 401        |
      | invalid  | password | 401        |

  @validate @negative
  Scenario Outline: Validate an invalid authentication token
    Given I have an invalid authentication token
    When I send a validate token request
    Then the response status code should be <statusCode>

    Examples:
      | statusCode |
      | 403       |

  @logout @negative
  Scenario Outline: Attempt to logout with invalid token
    Given I have an invalid authentication token
    When I send a logout request
    Then the response status code should be <statusCode>

    Examples:
      | statusCode |
      | 403       |

  @regression
  Scenario Outline: Complete authentication flow
    Given I have no active authentication token
    When I send a login request with username "<username>" and password "<password>"
    Then the response status code should be <loginStatus>
    And the response should contain a valid token
    When I send a validate token request
    Then the response status code should be <validateStatus1>
    When I send a logout request
    Then the response status code should be <logoutStatus>
    When I send a validate token request
    Then the response status code should be <validateStatus2>

    Examples:
      | username | password | loginStatus | validateStatus1 | logoutStatus | validateStatus2 |
      | admin    | password | 200         | 200            | 200          | 403            |
