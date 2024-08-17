package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// ** toJson() and accountsToJson() functions are modeled from the classes provided in GitHub below
// ** https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// Represents a bank customer, where a customer had a name and phone number.
// A bank customer has a list of bank accounts
public class Customer implements Writable {
    private final String name;
    private String phone;
    private final List<Account> accounts;

    // REQUIRES: name and phone number != null
    // EFFECTS: constructs the bank Customer, with given name and phone number
    //          also creates a list of bank accounts for the customer
    public Customer(String name, String phone) {
        this.name = name;
        this.phone = phone;
        accounts = new ArrayList<>();
    }

    // REQUIRES: account != null
    // MODIFIES: this
    // EFFECTS: adds a bank account to a customers list of bank accounts
    public void addAccount(Account account) {
        accounts.add(account);
        EventLog.getInstance().logEvent(new Event("Account " + account.getAccountNumber() + " added."));
    }


    // EFFECTS: checks what the largest account number in the list of account numbers is
    //          and return that accountNumber + 1
    public int getNextAccountNumber() {
        int nextNumber = 0;
        for (Account account : accounts) {
            if (account.getAccountNumber() > nextNumber) {
                nextNumber = account.getAccountNumber();
            }
        }
        return nextNumber + 1;
    }

    // REQUIRES: accountNumber > 0
    // MODIFIES: this
    // EFFECTS: if the given account number exists and the balance of the account is zero
    //          - removes a bank account with the given account number from the customers list of accounts
    //          - return true
    //          - otherwise, false
    public boolean removeAccount(int accountNumber) {
        for (Account account : accounts) {
            if (account.getBalance() == 0 && account.getAccountNumber() == accountNumber) {
                EventLog.getInstance().logEvent(new Event("Account " + account.getAccountNumber()
                        + " removed."));
                accounts.remove(account);
                return true;
            }
        }
        return false;
    }

    // REQUIRES: accountNumber > 0
    // MODIFIES: this
    // EFFECTS: if the given account number exists in the accounts list
    //          - return that account
    //          - otherwise, return null
    public Account getSingleAccount(int accNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber() == accNumber) {
                return account;
            }
        }
        return null;
    }

    // REQUIRES: phone != null
    // MODIFIES: this
    // EFFECTS: sets the phone to the given value
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return this.name;
    }

    public String getPhone() {
        return this.phone;
    }

    public List<Account> getAccounts() {
        return this.accounts;
    }

    // EFFECTS: returns this as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("phone number", phone);
        json.put("Accounts", accountsToJson());
        return json;
    }

    // EFFECTS: returns accounts in this customer as a JSON array
    private JSONArray accountsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Account a : accounts) {
            jsonArray.put(a.toJson());
        }

        return jsonArray;
    }

}
