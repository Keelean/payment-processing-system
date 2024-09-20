@make-payment
Feature: Make Payment
  As a customer
  I want to be able to make payment to a merchant
  So that he can offer me goods or services

  Scenario: Customer makes POST request to pay a merchant via card
    Given A customer provides his card details to make a purchase
    When He invokes the payment endpoint
    Then The payment is successful

  Scenario: Customer makes POST request to pay a merchant via bank trasnfer
    Given A customer provides his bank account details to make a purchase
    When He invokes the payment endpoint
    Then The payment is successful

