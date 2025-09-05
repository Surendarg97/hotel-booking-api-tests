@booking @get
Feature: Get Booking Details
  As a user of the booking system
  I want to retrieve booking details
  So that I can view reservation information

  Background:
    Given the booking API is available

  @smoke @positive
  Scenario: Successfully retrieve an existing booking using booking id
    Given a booking exists is available for room 3
    When I retrieve the booking details with booking id
    Then the response status code should be 200
    And the response should contain the correct booking information of room id 3

  @smoke @positive
  Scenario: Successfully retrieve an existing booking using room id
    Given a booking exists is available for room 2
    When I retrieve the booking details with room id 2
    Then the response status code should be 200
    And the response should contain the correct booking information of room id 2

  @positive
  Scenario: Successfully retrieve booking summary for a specific room
    When I retrieve the booking summary for room 3
    Then the response status code should be 200
    And the response should contain the room booking summary

  @positive
  Scenario: Successfully retrieve unavailable dates for a date range
    When I check for unavailable dates between "2025-12-01" and "2025-12-31"
    Then the response status code should be 200
    And the response should contain the list of unavailable dates

  @negative
  Scenario: Attempt to retrieve booking with invalid room id
    When I retrieve the booking details with room id 9999
    Then the response status code should be 404
    And the response should contain booking not found message

  @negative
  Scenario: Attempt to retrieve booking with invalid authentication token
    Given I have an invalid authentication token
    When I retrieve the booking details with room id 3
    Then the response status code should be 403
