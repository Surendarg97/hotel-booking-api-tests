@schema-validation @booking
Feature: Booking API Schema Validation
  As a QA Engineer
  I want to verify the API response schemas
  So that I can ensure the API contract is maintained

  Background:
    Given the booking API is available

  @smoke @create
  Scenario: Validate create booking response schema
    When I create a booking with the following details:
      | roomid | bookingid | firstname | lastname | depositpaid | email            | phone         | checkin    | checkout   |
      | 3   | 201      | Mary      | Nar      | false      | Mary@test.com    | 016583781545  | 2025-12-01 | 2025-12-05 |
    Then the response status code should be 200
    And the create booking response schema should be valid

  @smoke @get
  Scenario: Validate get booking response schema
    Given a booking exists is available for room 2
    When I retrieve the booking details with room id 2
    Then the response status code should be 200
    And the get booking response schema should be valid

  @smoke @update
  Scenario: Validate update booking response schema
    Given a booking exists is available for room 1
    When I update the booking with the following details:
      | roomid |  firstname | lastname | depositpaid | email            | phone         | checkin    | checkout   |
      | 1      |  win      | dune   | false        | sine@test.com    | 34564554567556  | 2025-11-09 | 2025-11-15 |
    Then the response status code should be 200
    And the update booking response schema should be valid

  @smoke @delete
  Scenario: Validate delete booking response schema
    Given a booking exists is available for room 3
    When I delete the booking
    Then the response status code should be 200
    And the delete booking response schema should be valid
