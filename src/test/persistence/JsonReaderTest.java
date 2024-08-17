package persistence;

import model.Account;
import model.Bank;
import model.Customer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {

    @Test
    void testReaderNonExistentFIle() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Bank bank = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyBank() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyBank.json");
        try {
            Bank bank = reader.read();
            assertEquals("My bank", bank.getName());
            assertEquals(0, bank.getCustomers().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }


    @Test
    void testReaderGeneralBank() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralBank.json");
        try {
            Bank bank = reader.read();
            assertEquals("Coast Capital", bank.getName());

            List<Customer> customers = bank.getCustomers();
            assertEquals(2, customers.size());
            assertEquals("matt", customers.get(0).getName());
            assertEquals("6049991111", customers.get(0).getPhone());
            assertEquals("jamie", customers.get(1).getName());
            assertEquals("6041119999", customers.get(1).getPhone());

            List<Account> customer1accounts = customers.get(0).getAccounts();
            List<Account> customer2accounts = customers.get(1).getAccounts();

            assertEquals(1, customer1accounts.get(0).getAccountNumber());
            assertEquals(477, customer1accounts.get(0).getBalance());
            assertEquals(5, customer1accounts.get(0).getTransactions().size());
            assertEquals(2, customer1accounts.get(1).getAccountNumber());
            assertEquals(189, customer1accounts.get(1).getBalance());
            assertEquals(3, customer1accounts.get(1).getTransactions().size());

            assertEquals(1, customer2accounts.get(0).getAccountNumber());
            assertEquals(80, customer2accounts.get(0).getBalance());
            assertEquals(3, customer2accounts.get(0).getTransactions().size());
            assertEquals(2, customer2accounts.get(1).getAccountNumber());
            assertEquals(300, customer2accounts.get(1).getBalance());
            assertEquals(0, customer2accounts.get(1).getTransactions().size());

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
