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