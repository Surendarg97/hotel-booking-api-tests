@booking @delete
Feature: Delete Booking
  As a user of the booking system
  I want to delete existing bookings
  So that I can remove unwanted reservations

  Background:
    Given the booking API is available

  @smoke @positive
  Scenario Outline: Successfully delete an existing booking
    Given I fetch a booking for room <roomId>
    When I delete the booking
    Then the response status code should be <statusCode>
    And the booking should be deleted successfully

    Examples:
      | roomId | statusCode |
      | 1      | 200       |

  @negative
  Scenario Outline: Attempt to delete booking with invalid token
    Given I fetch a booking for room <roomId>
    And I have an invalid authentication token
    When I delete the booking
    Then the response status code should be <statusCode>
    And the booking should still exist

    Examples:
      | roomId | statusCode |
      | 2      | 403       |

  @negative
  Scenario Outline: Attempt to delete a non-existent booking
    Given I have a non-existent booking ID <bookingId>
    When I delete the booking
    Then the response status code should be <statusCode>
    And the response should contain the validation error "<errorMessage>"

    Examples:
      | bookingId | statusCode | errorMessage      |
      | 99999     | 404       | Booking not found |