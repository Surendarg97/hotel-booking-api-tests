@booking @delete
Feature: Delete Booking
  As a user of the booking system
  I want to delete existing bookings
  So that I can remove unwanted reservations

  Background:
    Given the booking API is available

  @smoke @positive
  Scenario: Successfully delete an existing booking
    Given I fetch a booking for room 1
    When I delete the booking
    Then the response status code should be 200
    And the booking should be deleted successfully