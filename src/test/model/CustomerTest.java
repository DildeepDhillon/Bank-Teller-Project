package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class CustomerTest {
    Customer customer1;
    Customer customer2;
    Account account1;
    Account account2;
    Account account3;
    Account account4;

    Account account5;



    @BeforeEach
    void beforeEach() {
        customer1 = new Customer("bob", "6041112222");
        customer2 = new Customer("emily", "6042221111");
        account1 = new Account(1);
        account2 = new Account(2, 500);
        account3 = new Account(3);
        account4 = new Account(4, 250);
        account5 = new Account(8, 250);
    }

    @Test
    void constructorTest() {
        assertEquals("bob", customer1.getName());
        assertEquals("6041112222", customer1.getPhone());
        assertEquals(0, customer1.getAccounts().size());

        assertEquals("emily", customer2.getName());
        assertEquals("6042221111", customer2.getPhone());
        assertEquals(0, customer2.getAccounts().size());
    }

    @Test
    void addAccountTest() {
        customer1.addAccount(account1);
        customer1.addAccount(account2);
        customer2.addAccount(account3);
        customer2.addAccount(account4);

        assertEquals(2, customer1.getAccounts().size());
        assertEquals(2, customer2.getAccounts().size());

        assertEquals(account1, customer1.getAccounts().get(0));
        assertEquals(account2, customer1.getAccounts().get(1));

        assertEquals(account3, customer2.getAccounts().get(0));
        assertEquals(account4, customer2.getAccounts().get(1));
    }

    @Test
    void getNextAccountNumberTest() {
        customer1.addAccount(account1);
        customer1.addAccount(account2);
        customer2.addAccount(account3);
        customer2.addAccount(account4);

        assertEquals(3, customer1.getNextAccountNumber());
        assertEquals(5, customer2.getNextAccountNumber());

        customer2.addAccount(account5);
        assertEquals(9, customer2.getNextAccountNumber());
    }

    @Test
    void removeAccountTest() {
        customer1.addAccount(account1);
        customer1.addAccount(account2);
        customer2.addAccount(account3);
        customer2.addAccount(account4);

        assertFalse(customer1.removeAccount(3));
        assertTrue(customer1.removeAccount(1));

        assertFalse(customer2.removeAccount(5));
        assertTrue(customer2.removeAccount(3));

        assertEquals(1, customer1.getAccounts().size());
        assertEquals(1, customer2.getAccounts().size());

        assertEquals(account2, customer1.getAccounts().get(0));
        assertEquals(account4, customer2.getAccounts().get(0));
    }

    @Test
    void getSingleAccountTest() {
        customer1.addAccount(account1);
        customer1.addAccount(account2);
        customer2.addAccount(account3);
        customer2.addAccount(account4);

        assertNull(customer1.getSingleAccount(3));
        assertEquals(account2, customer1.getSingleAccount(2));

        assertNull(customer2.getSingleAccount(5));
        assertEquals(account4, customer2.getSingleAccount(4));
    }

    @Test
    void setPhoneTest() {
        assertEquals("6041112222", customer1.getPhone());
        assertEquals("6042221111", customer2.getPhone());

        customer1.setPhone("6049991111");
        assertEquals("6049991111", customer1.getPhone());

        customer2.setPhone("6041119999");
        assertEquals("6041119999", customer2.getPhone());
    }
}
