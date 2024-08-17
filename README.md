# Bank Database Application

## Introduction
<p> The application is designed to store a customer's information, such as 
his contact information, what bank he is using, and how many accounts he has open with this bank. 
Also, each account will hold a balance, and a list of transactions that have taken place.

A bank teller can use it to pull up the necessary information for a customer, and be able to make withdraws,
deposits, close or open accounts on the customers behalf. This project is of interest to me, because 
databases are needed almost everywhere, and banking is something everyone interacts with at least once a month.
So, this is application would be a small reflection, of what your bank teller could be using when processing 
your requests.</p>

## User Stories:
- As a user, I want to be able to add a new customer to a list of customers at a bank
- As a user, I want to be able to view all accounts for a customer
- As a user, I want to be able to add additional bank accounts to a customer's account
- As a user, I want to be able to select a customer's account and view the transactions that have occurred for that account
- As a user, when I quit the bank teller application, I want to be reminded to save the entire state of the bank teller application, including customer information, account details, and transaction history, to a file and have the option to do so or not. 
- As a user, I want to have the option to reload the saved state of the bank teller application from a file, allowing me to resume my banking activities exactly where I left off during a previous session.

## Phase 4: Task 2:
Sun Nov 26 01:49:18 PST 2023
Customer bobby added.

Sun Nov 26 01:49:23 PST 2023
Account 1 added.

Sun Nov 26 01:49:36 PST 2023
Deposited 50 to Account 1.

Sun Nov 26 01:49:41 PST 2023
Withdrew 10 from Account 1.

Sun Nov 26 01:49:47 PST 2023
Withdrew 10 from Account 1.

Sun Nov 26 01:49:56 PST 2023
Account 2 added.

Sun Nov 26 01:50:06 PST 2023
Deposited 50 to Account 2.

Sun Nov 26 01:50:11 PST 2023
Withdrew 10 from Account 2.

Sun Nov 26 01:50:41 PST 2023
Account 3 added.

Sun Nov 26 01:50:47 PST 2023
Account 3 removed.

Sun Nov 26 01:51:05 PST 2023
Customer milly added.

Sun Nov 26 01:51:13 PST 2023
Account 1 added.

Sun Nov 26 01:51:22 PST 2023
Deposited 1000 to Account 1.

Sun Nov 26 01:51:26 PST 2023
Withdrew 222 from Account 1.

## Phase 4: Task 3:
<p> There are some methods in the BankTellerUI class that should be private, as they
are only used in the class itself, and can prevent any unnecessary access by other users. 
</p>