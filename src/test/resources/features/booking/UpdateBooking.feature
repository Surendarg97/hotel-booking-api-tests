@booking @update
Feature: Update Booking Details
  As a user of the booking system
  I want to update existing bookings
  So that I can modify reservation details

  Background:
    Given the booking API is available

  @smoke @positive
  Scenario Outline: Successfully update an existing booking
    Given a booking exists is available for room <roomId>
    When I update the booking with the following details:
      | roomid    | firstname   | lastname   | depositpaid   | email           | phone         | checkin    | checkout   |
      | <roomId>  | <firstname> | <lastname> | <depositpaid> | <email>        | <phone>       | <checkin>  | <checkout> |
    Then the response status code should be <statusCode>
    And the booking should be updated successfully

    Examples:
      | roomId | firstname | lastname | depositpaid | email          | phone        | checkin    | checkout   | statusCode |
      | 2      | Jonath    | Shrif    | false      | shrif@test.com | 665198651966 | 2025-10-09 | 2025-10-15 | 200       |

  @negative
  Scenario Outline: Attempt to update booking with invalid booking ID
    Given I have a valid authentication token
    When I update booking with ID <bookingId> with the following details:
      | roomid    | firstname   | lastname   | depositpaid   | email           | phone         | checkin    | checkout   |
      | <roomId>  | <firstname> | <lastname> | <depositpaid> | <email>        | <phone>       | <checkin>  | <checkout> |
    Then the response status code should be <statusCode>
    And the response should contain booking not found message

    Examples:
      | bookingId | roomId | firstname | lastname | depositpaid | email         | phone      | checkin    | checkout   | statusCode |
      | 9999      | 2      | John      | Smith    | true       | john@test.com | 1234567890 | 2025-10-09 | 2025-10-15 | 404       |

  @negative
  Scenario Outline: Attempt to update booking with invalid token
    Given I fetch a booking for room <roomId>
    And I have an invalid authentication token
    When I update the booking with the following details:
      | roomid    | firstname   | lastname   | depositpaid   | email           | phone         | checkin    | checkout   |
      | <roomId>  | <firstname> | <lastname> | <depositpaid> | <email>        | <phone>       | <checkin>  | <checkout> |
    Then the response status code should be <statusCode>

    Examples:
      | roomId | firstname | lastname | depositpaid | email         | phone      | checkin    | checkout   | statusCode |
      | 2      | John      | Smith    | true       | john@test.com | 1234567890 | 2025-10-09 | 2025-10-15 | 403       |

  @negative
  Scenario Outline: Update booking should fail when required fields are missing
    Given I fetch a booking for room <existingRoomId>
    When I update the booking with the following details:
      | roomid   | firstname   | lastname   | depositpaid   | email   | phone   | checkin   | checkout   |
      | <roomid> | <firstname> | <lastname> | <depositpaid> | <email> | <phone> | <checkin> | <checkout> |
    Then the response status code should be <statusCode>
    And the response should contain the validation error "<expectedError>"

    Examples:
      | existingRoomId | firstname | lastname | depositpaid | email              | phone         | checkin    | checkout   | roomid | expectedError                                                    | statusCode |
      | 1             |          | Johnson  | true        | alice@example.com  | 987654321046  | 2025-09-15 | 2025-09-20 | 2      | Firstname should not be blank                                    | 400        |
      | 1             | Peter    |          | false       | emma@example.com   | 543210987624  | 2025-09-19 | 2025-09-24 | 6      | Lastname should not be blank                                     | 400        |
      | 1             | Richard  | Clark    | true        |                    | 210987654383  | 2025-09-23 | 2025-09-28 | 10     | Failed to create booking                                         | 400        |
      | 1             | Charles  | White    | false       | henry@example.com  |               | 2025-09-24 | 2025-09-29 | 11     | Failed to create booking                                         | 400        |
      | 1             | Joseph   | Moore    | true        | isaac@example.com  | 987654321085  |            | 2025-09-30 | 12     | must not be null                                                 | 400        |
      | 1             | Daniel   | Taylor   | false       | jack@example.com   | 876543210921  | 2025-09-25 |            | 13     | must not be null                                                 | 400        |
      | 1             |          |          | true        | noah@example.com   | 432109876502  | 2025-09-29 | 2025-09-30 | 16     | Firstname should not be blank, Lastname should not be blank      | 400        |

  @negative
  Scenario Outline: Update booking should fail when input values are invalid
    Given I fetch a booking for room <existingRoomId>
    When I update the booking with the following details:
      | roomid   | firstname   | lastname   | depositpaid   | email   | phone   | checkin   | checkout   |
      | <roomid> | <firstname> | <lastname> | <depositpaid> | <email> | <phone> | <checkin> | <checkout> |
    Then the response status code should be <statusCode>
    And the response should contain the validation error "<expectedError>"

    Examples:
      | existingRoomId | firstname | lastname | depositpaid | email              | phone         | checkin    | checkout   | roomid | expectedError                                                    | statusCode |
      | 1             | Al       | Kennedy  | false       | bob@example.com    | 876543210934  | 2025-09-16 | 2025-09-21 | 3      | size must be between 3 and 18                                    | 400        |
      | 1             | Robert   | @123     | true        | carol@example.com  | 765432109826  | 2025-09-17 | 2025-09-22 | 4      | Lastname should be a valid string                                | 400        |
      | 1             | $James   | Wilson   | false       | david@example.com  | 654321098752  | 2025-09-18 | 2025-09-23 | 5      | Firstname should be a valid string                               | 400        |
      | 1             | Michael  | Brown    | true        | invalid.email      | 432109876526  | 2025-09-20 | 2025-09-25 | 7      | must be a well-formed email address                              | 400        |
      | 1             | William  | Davis    | false       | frank@example.com  | 12345         | 2025-09-21 | 2025-09-26 | 8      | size must be between 11 and 21                                   | 400        |
      | 1             | Paul     | Anderson | true        | karen@example.com  | 765432109896  | 2020-09-26 | 2020-09-30 | 14     | Invalid booking dates                                            | 400        |
      | 1             | George   | Jackson  | false       | laura@example.com  | 654321098738  | 2025-09-27 | 2025-09-26 | 15     | Checkout date must be after checkin date                         | 400        |
      | 1             | Edward   | Harris   | true        | mark@example.com   | 543210987692  | 2025-09-28 | 2025-09-29 | 0      | must be greater than or equal to 1                               | 400        |

