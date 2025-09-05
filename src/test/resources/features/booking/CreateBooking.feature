@booking
Feature: Create Booking Functionality
  As a user of the booking system
  I want to create hotel bookings
  So that I can reserve rooms for specific dates

  Background: 
    Given the booking API is available

  @smoke @positive
  Scenario: Successfully create a valid booking
    When I create a booking with the following details:
      | roomid | bookingid | firstname | lastname | depositpaid | email            | phone         | checkin    | checkout   |
      | 1      | 101       | Wren      | Thrish   | true        | wren@test.com    | 016583781545  | 2025-09-08 | 2025-09-10 |
    Then the response status code should be 200
    And the booking should be created successfully
    And the response should contain the booking ID

  @negative
  Scenario Outline: Booking creation should fail with validation errors
    When I create a booking with the following details:
      | roomid   | firstname   | lastname   | depositpaid   | email   | phone   | checkin   | checkout   |
      | <roomid> | <firstname> | <lastname> | <depositpaid> | <email> | <phone> | <checkin> | <checkout> |
    Then the response status code should be 400
    And the response should contain the validation error "<expectedError>"

    Examples:
      | firstname | lastname | depositpaid | email             | phone        | checkin    | checkout   | roomid | expectedError                               |
      |          | Smith    | true        | john@test.com     | 1234567890   | 2025-09-08 | 2025-09-10 | 1      | Firstname should not be blank               |
      | Jo       | Smith    | true        | john@test.com     | 1234567890   | 2025-09-08 | 2025-09-10 | 1      | size must be between 3 and 18              |
      | John     | Smith    | true        | invalid-email     | 1234567890   | 2025-09-08 | 2025-09-10 | 1      | must be a well-formed email address        |
      | John     | Smith    | true        | john@test.com     | 1234567890   | 2020-09-08 | 2020-09-10 | 1      | Invalid booking dates                       |
      | John     | Smith    | true        | john@test.com     | 1234567890   | 2025-09-08 | 2025-09-10 | 0      | must be greater than or equal to 1         |
