@booking @update
Feature: Update Booking Details
  As a user of the booking system
  I want to update existing bookings
  So that I can modify reservation details

  Background:
    Given the booking API is available

  @smoke @positive
  Scenario: Successfully update an existing booking
    Given a booking exists is available for room 2
    When I update the booking with the following details:
      | roomid |  firstname | lastname | depositpaid | email            | phone         | checkin    | checkout   |
      | 2      |  Jonath      | Shrif   | false        | shrif@test.com    | 665198651966  | 2025-10-09 | 2025-10-15 |
    Then the response status code should be 200
    And the booking should be updated successfully

  @negative
  Scenario: Attempt to update booking with invalid booking ID
    Given I have a valid authentication token
    When I update booking with ID 9999 with the following details:
      | roomid | firstname | lastname | depositpaid | email          | phone        | checkin    | checkout   |
      | 2      | John     | Smith    | true        | john@test.com  | 1234567890   | 2025-10-09 | 2025-10-15 |
    Then the response status code should be 404
    And the response should contain booking not found message

  @negative
  Scenario: Attempt to update booking with invalid token
    Given I fetch a booking for room 2
    And I have an invalid authentication token
    When I update the booking with the following details:
      | roomid | firstname | lastname | depositpaid | email          | phone        | checkin    | checkout   |
      | 2      | John     | Smith    | true        | john@test.com  | 1234567890   | 2025-10-09 | 2025-10-15 |
    Then the response status code should be 403

  @negative
  Scenario: Attempt to update booking with missing mandatory parameters
    Given I fetch a booking for room 2
    When I update the booking with missing mandatory fields:
      | roomid | firstname | lastname | email          | phone      |
      | 2      | John     | Smith    | john@test.com  | 1234567890 |
    Then the response status code should be 400
    And the response should contain missing parameter message

