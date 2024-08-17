package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

// References the structure found in the tellerApp sample found in
// https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/main/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java
// Functions BankTellerApp, runBankTeller and process commands follow a similar logic to this example
public class BankTellerApp {
    private static final String JSON_STORE = "./data/myFile.json";
    private Bank bank1;
    private final Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the bank teller application
    public BankTellerApp() {
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        bank1 = new Bank();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runBankTeller();
    }

    // MODIFIES: this
    // EFFECTS: calls the initializer function and processes the user command for the main menu
    private void runBankTeller() {
        boolean keepGoing = true;
        String command;

        // commented out because we can now load bank info from a file, which acts like the initializer
        //initializer();

        while (keepGoing) {
            showMenuMain();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
                showMenuSave();
                String value = input.next();
                if (value.equals("y")) {
                    saveBankFile();
                } else {
                    System.out.println("Logging out, without saving!");
                    exit(0);
                }
            } else if (command.equals("l")) {
                loadFile();
            } else {
                runningCustomer(command);
            }
        }
    }

    // REQUIRES: customer1 != null
    // MODIFIES: this
    // EFFECTS: processes user commands for the customer menu
    private void runCustomer(Customer customer1) {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            showMenuCustomer();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("c")) {
                getAccounts(customer1);
            } else if (command.equals("p")) {
                changeNumber(customer1);
            } else if (command.equals("a")) {
                addAnAccount(customer1);
            } else if (command.equals("r")) {
                removeAnAccount(customer1);
            } else {
                keepGoing = false;
            }
        }
    }

    // REQUIRES: customer1 != null
    // MODIFIES: this
    // EFFECTS: processes user commands for the account menu
    private void getAccounts(Customer customer1) {
        boolean keepGoing = true;
        String command;
        Account account1;
        account1 = findAccount(customer1);

        while (keepGoing) {

            if (account1 != null) {
                showMenuAccounts();
                command = input.next();
                command = command.toLowerCase();

            } else {
                break;
            }

            if (command.equals("w")) {
                withdrawAccount(account1);
            } else if (command.equals("d")) {
                depositAccount(account1);
            } else if (command.equals("t")) {
                accountTransactions(account1);
            } else {
                keepGoing = false;
            }
        }
    }


    // MODIFIES: this
    // EFFECTS: initializes customers, accounts and the bank
    private void initializer() {
        Account account1Bob = new Account(1);
        Account account1Emily = new Account(1);
        Customer bob = new Customer("bob", "6041112222");
        bob.addAccount(account1Bob);
        Customer emily = new Customer("emily", "6042221111");
        emily.addAccount(account1Emily);
        bank1.addACustomer(bob);
        bank1.addACustomer(emily);
    }

    // EFFECTS: displays a menu of options
    private void showMenuMain() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> add new customer");
        System.out.println("\tf -> find a customer");
        System.out.println("\tl -> load bank information from file");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: displays a menu of options for a customer
    private void showMenuCustomer() {
        System.out.println("\nSelect from:");
        System.out.println("\tc -> get the customers accounts");
        System.out.println("\tp -> change the phone number");
        System.out.println("\ta -> add an account");
        System.out.println("\tr -> remove an account");
        System.out.println("\tq -> back to previous menu");

    }

    // EFFECTS: displays a menu of options for an account
    private void showMenuAccounts() {
        System.out.println("\nSelect from:");
        System.out.println("\tw -> withdraw");
        System.out.println("\td -> deposit");
        System.out.println("\tt -> get the list of transactions");
        System.out.println("\tq -> back to previous menu");
    }


    // REQUIRES: command != null
    // MODIFIES: this
    // EFFECTS: processes user commands for the main menu
    private Customer processCommandMain(String command) {
        if (command.equals("a")) {
            return addNewCustomer();
        } else if (command.equals("f")) {
            Customer customer1 = findCustomer();
            if (customer1 != null) {
                System.out.println("\nCustomer " + customer1.getName());
                return customer1;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    // MODIFIES: this
    // EFFECTS: add a new customer to the bank customers list
    private Customer addNewCustomer() {
        System.out.println("\nTo add a new customer:");
        String name;
        String phone;

        System.out.println("\nEnter the name: ");
        name = input.next();
        name = name.toLowerCase();

        System.out.println("\nEnter the phone number: ");
        phone = input.next();
        phone = phone.toLowerCase();

        Customer customer1 = new Customer(name, phone);
        System.out.println("\nCustomer added!");
        bank1.addACustomer(customer1);
        addAnAccount(customer1);
        return customer1;
    }

    // MODIFIES: this
    // EFFECTS: uses the customers name and phone number to find the customer in the bank customers list
    private Customer findCustomer() {
        System.out.println("\nTo find a customer:");
        String name;
        String phone;

        System.out.println("\nEnter the name: ");
        name = input.next();
        name = name.toLowerCase();

        System.out.println("\nEnter the phone number: ");
        phone = input.next();
        phone = phone.toLowerCase();

        return bank1.findACustomer(name, phone);
    }

    // REQUIRES: customer1 != null
    // MODIFIES: this
    // EFFECTS: uses a customers account number to return that account
    private Account findAccount(Customer customer1) {
        Account account1;
        showAccounts(customer1);
        System.out.println("\nEnter the account number you wish to proceed with: ");
        int accNumber = Integer.parseInt(input.next());
        account1 = customer1.getSingleAccount(accNumber);
        return account1;
    }

    // REQUIRES: customer1 != null
    // MODIFIES: this, customer
    // EFFECTS: changes the phone number for a customer
    private void changeNumber(Customer customer1) {
        System.out.println("\nEnter the new phone number: ");
        String phone = input.next();
        phone = phone.toLowerCase();

        customer1.setPhone(phone);
    }

    // REQUIRES: customer1 != null
    // MODIFIES: this, customer
    // EFFECTS: adds an account to the customers account list,
    //          if the customer had an initial balance we call the overloaded constructor for account
    //          otherwise, we use other account constructor
    private void addAnAccount(Customer customer1) {
        Account account1;
        int accNumber = customer1.getNextAccountNumber();
        System.out.println("\nDo you want to deposit any money in this account right away: ");
        System.out.println("\t 1 -> yes");
        System.out.println("\t 2 -> no");
        int answer = Integer.parseInt(input.next());
        if (answer == 1) {
            System.out.println("\nEnter the amount you want to deposit: ");
            int balance = Integer.parseInt(input.next());
            account1 = new Account(accNumber, balance);
            System.out.println("\nAccount with a balance of " + balance + " created");
        } else {
            account1 = new Account(accNumber);
            System.out.println("\nAccount with no balance created");
        }
        customer1.addAccount(account1);
    }

    // REQUIRES: customer1 != null
    // MODIFIES: this, customer
    // EFFECTS: removes the account the user specifies from the customers account list
    //          if the balance for the account is zero,
    //          otherwise, it does not remove it
    private void removeAnAccount(Customer customer1) {
        showAccounts(customer1);
        System.out.println("\nEnter the account number you wish to remove: ");
        int accNumber = Integer.parseInt(input.next());
        if (customer1.removeAccount(accNumber)) {
            System.out.println("\nSuccessfully removed the account!");
        } else {
            System.out.println("\nError in removing account!");
            System.out.println("Invalid account number or account has a balance!");
        }
    }

    // REQUIRES: customer1 != null
    // MODIFIES: this
    // EFFECTS: shows all the accounts for the given customer
    private void showAccounts(Customer customer1) {
        List<Account> accountsList;
        accountsList = customer1.getAccounts();
        int i = 1;
        System.out.println("\nAccounts for Customer " + customer1.getName() + ":");
        for (Account account1 : accountsList) {
            System.out.println("\n" + i + ") " + "Account number: " + account1.getAccountNumber()
                    + "\tBalance: " + account1.getBalance());
            i++;
        }
    }

    // REQUIRES: account1 != null
    // MODIFIES: this, account
    // EFFECTS: withdraws the user specified amount from the given account
    //          if the account has sufficient balance,
    //          otherwise, it does not withdraw the money
    private void withdrawAccount(Account account1) {
        System.out.println("\nEnter the amount you want to withdraw: ");
        int amount = Integer.parseInt(input.next());

        if (account1.withdraw(amount)) {
            System.out.println("\n" + amount + " successfully withdrawn");
            System.out.println("\nNew balance is: " + account1.getBalance());
        } else {
            System.out.println("\nInsufficient amount of funds!");
            System.out.println("\nCurrent balance is: " + account1.getBalance());
        }
    }

    // REQUIRES: account1 != null
    // MODIFIES: this, account
    // EFFECTS: deposits the user specified amount from the given account
    private void depositAccount(Account account1) {
        System.out.println("\nEnter the amount you want to deposit: ");
        int amount = Integer.parseInt(input.next());
        account1.deposit(amount);
        System.out.println("\n" + amount + " successfully deposited");
        System.out.println("\nNew balance is: " + account1.getBalance());
    }

    // REQUIRES: account1 != null
    // MODIFIES: this
    // EFFECTS: displays all transactions for the given account
    private void accountTransactions(Account account1) {
        List<String> transactionsList;
        transactionsList = account1.getTransactions();
        int i = 1;
        System.out.println("\nTransactions for Account " + account1.getAccountNumber() + ":");
        for (String transactions : transactionsList) {
            System.out.println("\n" + i + ") " + transactions);
            i++;
        }
    }

    // EFFECTS: displays menu for saving the state of the bank
    private void showMenuSave() {
        System.out.println("Do you want to save the bank information before you quit!");
        System.out.println("\ty -> yes");
        System.out.println("\tn -> no");
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void saveBankFile() {
        try {
            System.out.println("\nEnter the banks name:");
            String name = input.next();
            bank1.setName(name);
            jsonWriter.open();
            jsonWriter.write(bank1);
            jsonWriter.close();
            System.out.println("Saved " + bank1.getName() + " to " + JSON_STORE);
            System.out.println("Logging out!");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void loadBankFile() {
        try {
            bank1 = jsonReader.read();
            System.out.println("Loaded " + bank1.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void loadFile() {
        loadBankFile();
        runBankTeller();
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void runningCustomer(String command) {
        Customer customer1 = processCommandMain(command);
        if (customer1 == null) {
            System.out.println("\nUnable to find customer, back to the main menu");
            return;
        }
        runCustomer(customer1);
    }
}
