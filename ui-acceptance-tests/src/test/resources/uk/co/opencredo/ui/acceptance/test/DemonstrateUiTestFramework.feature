@demo @ui
Feature: Demonstrate the UI test framework

  Scenario: Simple interaction with a web page
    Given I am on the Google search page
    When I search for "OpenCredo"
    Then there the site "www.opencredo.com" should be present in the results