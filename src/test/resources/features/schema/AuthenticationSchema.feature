@authentication @schema
Feature: Authentication API Schema Validation
  As an API developer
  I want to ensure the authentication endpoints return correct response schemas
  So that client applications can reliably parse the responses

  Background:
    Given the authentication API is available

  @login @schema
  Scenario: Validate login endpoint response schema
    When I send a login request with username "admin" and password "password"
    Then the response status code should be 200
    And the login response schema should be valid

  @validate @schema
  Scenario: Validate token validation endpoint response schema
    Given I have a valid authentication token
    When I send a validate token request
    Then the response status code should be 200
    And the token validation response schema should be valid

  @logout @schema
  Scenario: Validate logout endpoint response schema
    Given I have a valid authentication token
    When I send a logout request
    Then the response status code should be 200
    And the logout response schema should be valid
