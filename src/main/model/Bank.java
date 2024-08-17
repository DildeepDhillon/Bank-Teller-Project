package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// ** toJson() and customersToJson() functions are modeled from the classes provided in GitHub below
// ** https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// Represents a bank, where a bank has a list of customers
public class Bank implements Writable {
    private String name;
    private final List<Customer> customers;


    // EFFECTS: constructs the bank, by creating the list to store the customers
    //          and with the given name
    public Bank(String name) {
        this.name = name;
        customers = new ArrayList<>();
    }

    // Overloaded constructor, so we have the option to create a bank without a name
    // EFFECTS: constructs the bank, by creating the list to store the customers
    public Bank() {
        this.name = null;
        customers = new ArrayList<>();
    }

    // REQUIRES: customer1 != null
    // MODIFIES: this
    // EFFECTS: adds a customer to the banks list of customers
    public void addACustomer(Customer customer1) {
        customers.add(customer1);
        EventLog.getInstance().logEvent(new Event("Customer " + customer1.getName() + " added."));
    }

    // REQUIRES: name & phone != null
    // EFFECTS: if the name and phone number match a customer in the customers list
    //          - return the customer
    //          - otherwise, return null
    public Customer findACustomer(String name, String phone) {
        for (Customer customer1 : customers) {
            if (customer1.getName().equals(name) && customer1.getPhone().equals(phone)) {
                EventLog.getInstance().logEvent(new Event("Customer " + customer1.getName() + " found."));
                return customer1;
            }
        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public List<Customer> getCustomers() {
        return this.customers;
    }

    // EFFECTS: returns this as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Bank name", name);
        json.put("Customers", customersToJson());
        return json;
    }

    // EFFECTS: returns customers in this bank as a JSON array
    private JSONArray customersToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Customer c : customers) {
            jsonArray.put(c.toJson());
        }

        return jsonArray;
    }
}
