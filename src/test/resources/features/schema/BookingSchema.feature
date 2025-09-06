@schema @booking
Feature: Booking API Schema Validation
  As a QA Engineer
  I want to verify the API response schemas
  So that I can ensure the API contract is maintained

  Background:
    Given the booking API is available

  @smoke @create
  Scenario Outline: Validate create booking response schema
    When I create a booking with the following details:
      | roomid    | bookingid    | firstname   | lastname   | depositpaid   | email   | phone   | checkin   | checkout   |
      | <roomId>  | <bookingId>  | <firstname> | <lastname> | <depositpaid> | <email> | <phone> | <checkin> | <checkout> |
    Then the response status code should be <statusCode>
    And the create booking response schema should be valid

    Examples:
      | roomId | bookingId | firstname | lastname | depositpaid | email         | phone        | checkin    | checkout   | statusCode |
      | 3      | 201       | Mary      | Nar      | false      | Mary@test.com | 016583781545 | 2025-12-01 | 2025-12-05 | 200       |

  @smoke @get
  Scenario Outline: Validate get booking response schema
    Given a booking exists is available for room <roomId>
    When I retrieve the booking details with room id <roomId>
    Then the response status code should be <statusCode>
    And the get booking response schema should be valid

    Examples:
      | roomId | statusCode |
      | 2      | 200       |

  @smoke @update
  Scenario Outline: Validate update booking response schema
    Given a booking exists is available for room <roomId>
    When I update the booking with the following details:
      | roomid    | firstname   | lastname   | depositpaid   | email   | phone   | checkin   | checkout   |
      | <roomId>  | <firstname> | <lastname> | <depositpaid> | <email> | <phone> | <checkin> | <checkout> |
    Then the response status code should be <statusCode>
    And the update booking response schema should be valid

    Examples:
      | roomId | firstname | lastname | depositpaid | email         | phone          | checkin    | checkout   | statusCode |
      | 1      | win       | dune     | false      | sine@test.com | 34564554567556 | 2025-11-09 | 2025-11-15 | 200       |

  @smoke @delete
  Scenario Outline: Validate delete booking response schema
    Given a booking exists is available for room <roomId>
    When I delete the booking
    Then the response status code should be <statusCode>
    And the delete booking response schema should be valid

    Examples:
      | roomId | statusCode |
      | 3      | 200       |
