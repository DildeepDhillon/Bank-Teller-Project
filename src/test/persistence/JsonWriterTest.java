package persistence;

import model.Account;
import model.Bank;
import model.Customer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Bank bank = new Bank("My bank");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyBank() {
        try {
            Bank bank = new Bank("My bank");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyBank.json");
            writer.open();
            writer.write(bank);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyBank.json");
            bank = reader.read();
            assertEquals("My bank", bank.getName());
            assertEquals(0, bank.getCustomers().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    private Bank initialize() {
        Bank bank = new Bank("My bank");
        Customer customer1 = new Customer("kary", "6041117777");
        Customer customer2 = new Customer("nick", "6048881111");
        bank.addACustomer(customer1);
        bank.addACustomer(customer2);

        Account account1 = new Account(1);
        account1.deposit(500);
        account1.withdraw(33);
        account1.withdraw(88);
        customer1.addAccount(account1);
        Account account2 = new Account(2);
        account2.deposit(200);
        account2.withdraw(18);
        account2.withdraw(34);
        account2.withdraw(11);
        account2.withdraw(19);
        customer1.addAccount(account2);
        Account account3 = new Account(1);
        account3.deposit(300);
        account3.withdraw(111);
        account3.withdraw(12);
        account3.withdraw(18);
        customer2.addAccount(account3);

        return bank;
    }

    @Test
    void testWriterGeneralBank() {
        try {
            Bank bank = initialize();
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralBank.json");
            writer.open();
            writer.write(bank);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralBank.json");
            bank = reader.read();
            assertEquals("My bank", bank.getName());

            List<Customer> customers = bank.getCustomers();
            assertEquals(2, customers.size());
            assertEquals("kary", customers.get(0).getName());
            assertEquals("6041117777", customers.get(0).getPhone());
            assertEquals("nick", customers.get(1).getName());
            assertEquals("6048881111", customers.get(1).getPhone());

            List<Account> customer1accounts = customers.get(0).getAccounts();
            List<Account> customer2accounts = customers.get(1).getAccounts();

            assertEquals(1, customer1accounts.get(0).getAccountNumber());
            assertEquals(379, customer1accounts.get(0).getBalance());
            assertEquals(3, customer1accounts.get(0).getTransactions().size());
            assertEquals(2, customer1accounts.get(1).getAccountNumber());
            assertEquals(118, customer1accounts.get(1).getBalance());
            assertEquals(5, customer1accounts.get(1).getTransactions().size());

            assertEquals(1, customer2accounts.get(0).getAccountNumber());
            assertEquals(159, customer2accounts.get(0).getBalance());
            assertEquals(4, customer2accounts.get(0).getTransactions().size());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }

    }
}
