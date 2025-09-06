@authentication @schema
Feature: Authentication API Schema Validation
  As an API developer
  I want to ensure the authentication endpoints return correct response schemas
  So that client applications can reliably parse the responses

  Background:
    Given the authentication API is available

  @login @schema
  Scenario Outline: Validate login endpoint response schema
    When I send a login request with username "<username>" and password "<password>"
    Then the response status code should be <statusCode>
    And the login response schema should be valid

    Examples:
      | username | password | statusCode |
      | admin    | password | 200       |

  @validate @schema
  Scenario Outline: Validate token validation endpoint response schema
    Given I have a valid authentication token
    When I send a validate token request
    Then the response status code should be <statusCode>
    And the token validation response schema should be valid

    Examples:
      | statusCode |
      | 200       |

  @logout @schema
  Scenario Outline: Validate logout endpoint response schema
    Given I have a valid authentication token
    When I send a logout request
    Then the response status code should be <statusCode>
    And the logout response schema should be valid

    Examples:
      | statusCode |
      | 200       |
