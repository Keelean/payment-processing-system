@estate-management
Feature: Make Payment
  As a customer
  I want to be able to make payment to a merchant
  So that he can offer me goods or services

  Scenario: Customer makes POST request to pay a merchant via card
    Given A customer provides his card details including the amount his paying for the goods or services and his PIN
    When He invokes the payment endpoint
    Then The payment is successful