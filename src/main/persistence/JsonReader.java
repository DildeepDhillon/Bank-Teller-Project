package persistence;

import model.Account;
import model.Bank;
import model.Customer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

// ** JsonReader class is modeled from the classes provided in GitHub below
// ** https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// Represents a JsonReader object, that reads the data of a Json file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }


    // EFFECTS: reads bank from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Bank read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseBank(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses bank from JSON object and returns it
    private Bank parseBank(JSONObject jsonObject) {
        String name = jsonObject.getString("Bank name");
        Bank bank = new Bank(name);
        addCustomers(bank, jsonObject);
        return bank;
    }

    // EFFECTS: parses customers from JSON object
    private void addCustomers(Bank bank, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Customers");
        for (Object json : jsonArray) {
            JSONObject nextCustomer = (JSONObject) json;
            addCustomer(bank,nextCustomer);
        }
    }

    // MODIFIES: bank
    // EFFECTS: Adds customer data from the JSON object to the bank
    //          parses accounts from JSON object
    private void addCustomer(Bank bank, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String phone = jsonObject.getString("phone number");
        Customer customer = new Customer(name, phone);
        bank.addACustomer(customer);
        JSONArray jsonArray = jsonObject.getJSONArray("Accounts");
        for (Object json : jsonArray) {
            JSONObject nextAccount = (JSONObject) json;
            addAccount(customer,nextAccount);
        }
    }

    // MODIFIES: customer, account
    // EFFECTS: Adds account data from the JSON object to the customer
    //          parses transactions from JSON object and add these transactions to the account
    private void addAccount(Customer customer, JSONObject jsonObject) {
        int accNumber = jsonObject.getInt("Account Number");
        int balance = jsonObject.getInt("balance");
        Account account = new Account(accNumber, balance);
        customer.addAccount(account);

        JSONArray transactionsArray = jsonObject.getJSONArray("transactions");
        for (Object transaction : transactionsArray) {
            String transactionString = (String) transaction;
            account.addTransactions(transactionString);
        }
    }
}
