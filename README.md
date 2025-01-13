**Known issues and bugs**

1. Bought seats are not set as occupied in database. Same seat can be sold multiple times
2. When buying a product, its stock isn't checked. Product with no stock can be added to cart and bought
3. Stocks for products are not decremented on the database when they are bought
4. Selected seats are not displayed in schopping cart
5. In shopping cart sometimes more than 2 decimal points are displayed due to BigDecimal division
6. Seat selection only works for Oppenheimer session at 2025-01-10 18:00 in Hall 1. Data for other sessions and seats are not initialized in database
7. In manager menu total revenue and the tax amounts to be paid not implemented
8. Customer requests for cancellations and refunds not implemenetd
9. Checkout button in shopping cart doesn't close the checkout menu and return to main cashier menu.
