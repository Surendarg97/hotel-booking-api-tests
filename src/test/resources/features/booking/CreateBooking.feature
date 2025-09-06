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
  Scenario Outline: Booking creation should fail when required fields are missing
    When I create a booking with the following details:
      | roomid   | firstname   | lastname   | depositpaid   | email   | phone   | checkin   | checkout   |
      | <roomid> | <firstname> | <lastname> | <depositpaid> | <email> | <phone> | <checkin> | <checkout> |
    Then the response status code should be 400
    And the response should contain the validation error "<expectedError>"

    Examples:
      | firstname| lastname | depositpaid | email              | phone         | checkin    | checkout   | roomid | expectedError                                                    |
      |          | Johnson  | true        | alice@example.com  | 987654321046  | 2025-09-15 | 2025-09-20 | 2      | Firstname should not be blank                                    |
      | Peter    |          | false       | emma@example.com   | 543210987624  | 2025-09-19 | 2025-09-24 | 6      | Lastname should not be blank                                     |
      | Richard  | Clark    | true        |                    | 210987654383  | 2025-09-23 | 2025-09-28 | 10     | Failed to create booking                                         |
      | Charles  | White    | false       | henry@example.com  |               | 2025-09-24 | 2025-09-29 | 11     | Failed to create booking                                         |
      | Joseph   | Moore    | true        | isaac@example.com  | 987654321085  |            | 2025-09-30 | 12     | must not be null                                                 |
      | Daniel   | Taylor   | false       | jack@example.com   | 876543210921  | 2025-09-25 |            | 13     | must not be null                                                 |
      |          |          | true        | noah@example.com   | 432109876502  | 2025-09-29 | 2025-09-30 | 16     | Firstname should not be blank, Lastname should not be blank      |

  @negative
  Scenario Outline: Booking creation should fail when input values are invalid
    When I create a booking with the following details:
      | roomid   | firstname   | lastname   | depositpaid   | email   | phone   | checkin   | checkout   |
      | <roomid> | <firstname> | <lastname> | <depositpaid> | <email> | <phone> | <checkin> | <checkout> |
    Then the response status code should be 400
    And the response should contain the validation error "<expectedError>"

    Examples:
      | firstname| lastname | depositpaid  | email             | phone         | checkin    | checkout   | roomid | expectedError                                                    |
      | Al       | Kennedy  | false        | bob@example.com   | 876543210934  | 2025-09-16 | 2025-09-21 | 3      | size must be between 3 and 18                                    |
      | Robert   | @123     | true         | carol@example.com | 765432109826  | 2025-09-17 | 2025-09-22 | 4      | Lastname should be a valid string                                |
      | $James   | Wilson   | false        | david@example.com | 654321098752  | 2025-09-18 | 2025-09-23 | 5      | Firstname should be a valid string                               |
      | Michael  | Brown    | true         | invalid.email     | 432109876526  | 2025-09-20 | 2025-09-25 | 7      | must be a well-formed email address                              |
      | William  | Davis    | false        | frank@example.com | 12345         | 2025-09-21 | 2025-09-26 | 8      | size must be between 11 and 21                                   |
      | Paul     | Anderson | true         | karen@example.com | 765432109896  | 2020-09-26 | 2020-09-30 | 14     | Invalid booking dates                                            |
      | George   | Jackson  | false        | laura@example.com | 654321098738  | 2025-09-27 | 2025-09-26 | 15     | Checkout date must be after checkin date                         |
      | Edward   | Harris   | true         | mark@example.com  | 543210987692  | 2025-09-28 | 2025-09-29 | 0      | must be greater than or equal to 1                               |
