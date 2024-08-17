package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// ** toJson() and transactionsToJson() functions are modeled from the classes provided in GitHub below
// ** https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// Represents a bank account, where we can withdraw or deposit money from the account
public class Account implements Writable {
    private int balance;
    private final int accountNumber;
    private final List<String> transactions;

    // REQUIRES: accountNumber > 0
    // EFFECTS: constructs the account with the balance set to $0 and the given account number
    // and creates a list for keeping track of transactions
    public Account(int accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = 0;
        this.transactions = new ArrayList<>();
    }

    // Overloaded constructor, so we have the option to create an account with a balance of zero
    // or give an account an initial balance
    // REQUIRES: accountNumber > 0 and initialBalance > 0
    // EFFECTS: constructs the account with the balance set to the given amount and the given account number
    // and creates a list for keeping track of transactions
    public Account(int accountNumber, int initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
    }

    // REQUIRES: value > 0
    // MODIFIES: this
    // EFFECTS: adds the value to the balance, and updates the transactions list
    public void deposit(int value) {
        this.balance += value;
        this.transactions.add("+" + value);
        EventLog.getInstance().logEvent(new Event("Deposited " + value + " to Account "
                + accountNumber + "."));
    }

    // REQUIRES: value > 0
    // MODIFIES: this
    // EFFECTS: if there is sufficient balance on the account
    //              - subtracts the value from the balance
    //              - updates the transactions list
    //              - returns true
    //              otherwise, return false
    public boolean withdraw(int value) {
        if (this.balance >= value) {
            this.balance -= value;
            this.transactions.add("-" + value);
            EventLog.getInstance().logEvent(new Event("Withdrew " + value + " from Account "
                    + accountNumber + "."));
            return true;
        } else {
            return false;
        }

    }

    public int getAccountNumber() {
        return this.accountNumber;
    }

    public int getBalance() {
        return this.balance;
    }

    public List<String> getTransactions() {
        return this.transactions;
    }

    // REQUIRES: transaction != null
    // MODIFIES: this
    // EFFECTS: adds a transaction to the list of transactions for the account
    public void addTransactions(String transaction) {
        this.transactions.add(transaction);
    }

    // EFFECTS: returns this as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Account Number", accountNumber);
        json.put("balance", balance);
        json.put("transactions", transactionsToJson());
        return json;
    }

    // EFFECTS: returns transactions in this account as a JSON array
    private JSONArray transactionsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (String s : transactions) {
            jsonArray.put(s);
        }

        return jsonArray;
    }

    // References: https://stackoverflow.com/questions/24179793/java-how-to-make-jcombobox-of-non-string-objects-display-string-names
    // Reference helped understand how to display the account objects correctly in the combo box
    // EFFECTS: override toString method, so combo box will display account number and account balance instead.
    @Override
    public String toString() {
        return "Account " + getAccountNumber() + " Balance " + getBalance();
    }
}
