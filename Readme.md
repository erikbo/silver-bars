# Silver-bars
Implementation made by Erik Bohlin

## Assumptions:
- Orders with quantity 0 are not allowed. I.e. we disallow orders that are not buying or selling anything.
- Orders with negative quantity are not allowed. I.e. The correct OrderType needs to be used.
- Orders with 0 price per kilo are not allowed, I.e. free orders are not allowed for buying or selling.
- Orders with negative price per kilo are not allowed.
- An order is considered equal to an other order iff all the fields are equal.
- Prices can only be given in whole numbers (int), the quantity can be given as a double.
- If there are no orders registered the summary method will return an empty list.

## Design Decisions:
- There are a few ways of interpreting the requirements regarding the return type of the summary information.
  I have opted to interpret it as requesting the return type of a List<String> instead of a multiline String, my reasoning
  is that the UI team will have a much easier time with formatting etc if they get a list.

- There is a number of ways of choosing the backing data structure for this assignment, for example a Map<OrderType, List<Order>>
  would be another decent choice. I decided to use a pure List to keep the registration and cancellation methods short and concise.

- I opted to not make the backing list static, as to allow for multiple markets, i.e maybe one for Europe, one for North America
  and so on.

- Some of the object and attribute validation could be made slightly easier by using an external library such as Guava.
  However as the artifact will be shipped as a library I opted to not use these to keep the artifact size down and avoid
  potential issues with transient dependencies.

- I have made the decision to use standard Java unchecked exception (IllegalArgumentException and NoSuchElementException) instead
  of implementing custom exceptions. While a custom exception might convey slightly more information I considered it over engineering
  for this test.

- I decided to use JUnit theories (https://github.com/junit-team/junit4/wiki/Theories) for testing the validation of
  the order registration. This is instead of creating multiple different test methods with different orders.