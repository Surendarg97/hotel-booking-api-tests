@booking @deletebooking
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

  @negative
  Scenario: Attempt to delete booking with invalid token
    Given I fetch a booking for room 2
    And I have an invalid authentication token
    When I delete the booking
    Then the response status code should be 403
    And the booking should still exist

  @negative
  Scenario: Attempt to delete a non-existent booking
    Given I have a non-existent booking ID 99999
    When I delete the booking
    Then the response status code should be 404
    And the response should contain the validation error "Booking not found"