package ui;

import model.Account;
import javax.swing.*;

// Class lets user create a custom JButton, that stores an account, and it's labeled with the account details
// References this site: https://www.javatpoint.com/java-jbutton
public class AccountButton extends JButton {
    private Account account;

    public AccountButton(Account account) {
        // super constructor creates the name/label for the button
        super("Account Number: " + account.getAccountNumber() + " Balance: " + account.getBalance());
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }
}
