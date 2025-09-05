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