package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BankTest {
    Bank bank1;
    Customer customer1;
    Customer customer2;

    @BeforeEach
    void beforeEach() {
        bank1 = new Bank("Meridian");
        customer1 = new Customer("bob", "6041112222");
        customer2 = new Customer("emily", "6042221111");
    }

    @Test
    void constructorTest() {
        assertEquals(0, bank1.getCustomers().size());
        assertEquals("Meridian", bank1.getName());

        Bank bank2 = new Bank();
        assertEquals(0, bank2.getCustomers().size());
        assertNull(bank2.getName());
    }

    @Test
    void addACustomerTest() {
        bank1.addACustomer(customer1);
        bank1.addACustomer(customer2);

        assertEquals(2, bank1.getCustomers().size());

        assertEquals(customer1, bank1.getCustomers().get(0));
        assertEquals(customer2, bank1.getCustomers().get(1));
    }

    @Test
    void findACustomerTest() {
        bank1.addACustomer(customer1);
        bank1.addACustomer(customer2);

        assertNull(bank1.findACustomer("john", "6041112222"));
        assertEquals(customer1,bank1.findACustomer("bob", "6041112222"));
        assertEquals(customer2,bank1.findACustomer("emily", "6042221111"));
    }

    @Test
    void setNameTest() {
        Bank bank2 = new Bank();

        assertNull(bank2.getName());
        bank2.setName("Bank Test");
        assertEquals("Bank Test",bank2.getName());
    }
}
