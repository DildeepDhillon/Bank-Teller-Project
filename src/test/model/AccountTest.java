package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    Account account1;
    Account account2;

    @BeforeEach
    void beforeEach() {
        account1 = new Account(1);
        account2 = new Account(2, 500);
    }

    @Test
    void constructorTest() {
        assertEquals(1, account1.getAccountNumber());
        assertEquals(0, account1.getBalance());
        assertEquals(0, account1.getTransactions().size());

        assertEquals(2, account2.getAccountNumber());
        assertEquals(500, account2.getBalance());
        assertEquals(0, account2.getTransactions().size());
    }

    @Test
    void depositTest() {
        account1.deposit(250);
        account2.deposit(500);

        assertEquals(250, account1.getBalance());
        assertEquals(1000, account2.getBalance());

    }

    @Test
    void withdrawTest() {
        assertFalse(account1.withdraw(20));
        assertTrue(account2.withdraw(200));
        assertFalse(account2.withdraw(301));
        assertEquals(300, account2.getBalance());
    }

    @Test
    void transactionsTest() {
        account1.deposit(250);
        account2.deposit(500);
        account1.withdraw(20);
        account2.withdraw(500);

        List<String> transactions1 = account1.getTransactions();
        List<String> transactions2 = account2.getTransactions();

        assertEquals(2, transactions1.size());
        assertEquals(2, transactions2.size());

        assertEquals("+" + 250, transactions1.get(0));
        assertEquals("-" + 20, transactions1.get(1));

        assertEquals("+" + 500, transactions2.get(0));
        assertEquals("-" + 500, transactions2.get(1));
    }

    @Test
    void addTransactionsTest() {
        account1.addTransactions("+250");
        account1.addTransactions("-50");

        List<String> transactions1 = account1.getTransactions();

        assertEquals(2, transactions1.size());

        assertEquals("+" + 250, transactions1.get(0));
        assertEquals("-" + 50, transactions1.get(1));

    }

    @Test
    void toStringTest() {
        assertEquals("Account " + account1.getAccountNumber() + " Balance "
                + account1.getBalance(), account1.toString());

        assertEquals("Account " + account2.getAccountNumber() + " Balance "
                + account2.getBalance(), account2.toString());

    }
}