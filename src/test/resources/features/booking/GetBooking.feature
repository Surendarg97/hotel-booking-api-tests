@booking @get
Feature: Get Booking Details
  As a user of the booking system
  I want to retrieve booking details
  So that I can view reservation information

  Background:
    Given the booking API is available

  @smoke @positive
  Scenario Outline: Successfully retrieve an existing booking using booking id
    Given a booking exists is available for room <roomId>
    When I retrieve the booking details with booking id
    Then the response status code should be <statusCode>
    And the response should contain the correct booking information of room id <roomId>

    Examples:
      | roomId | statusCode |
      | 3      | 200       |

  @smoke @positive
  Scenario Outline: Successfully retrieve an existing booking using room id
    Given a booking exists is available for room <roomId>
    When I retrieve the booking details with room id <roomId>
    Then the response status code should be <statusCode>
    And the response should contain the correct booking information of room id <roomId>

    Examples:
      | roomId | statusCode |
      | 2      | 200       |

  @positive
  Scenario Outline: Successfully retrieve booking summary for a specific room
    When I retrieve the booking summary for room <roomId>
    Then the response status code should be <statusCode>
    And the response should contain the room booking summary

    Examples:
      | roomId | statusCode |
      | 3      | 200       |

  @positive
  Scenario Outline: Successfully retrieve unavailable dates for a date range
    When I check for unavailable dates between "<startDate>" and "<endDate>"
    Then the response status code should be <statusCode>
    And the response should contain the list of unavailable dates

    Examples:
      | startDate   | endDate     | statusCode |
      | 2025-12-01 | 2025-12-31 | 200       |

  @negative
  Scenario Outline: Attempt to retrieve booking with invalid room id
    When I retrieve the booking details with room id <roomId>
    Then the response status code should be <statusCode>
    And the response should contain booking not found message

    Examples:
      | roomId | statusCode |
      | 9999   | 404       |

  @negative
  Scenario Outline: Attempt to retrieve booking with invalid authentication token
    Given I have an invalid authentication token
    When I retrieve the booking details with room id <roomId>
    Then the response status code should be <statusCode>

    Examples:
      | roomId | statusCode |
      | 3      | 403       |
