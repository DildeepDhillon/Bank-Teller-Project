package ui;

import model.Account;
import model.Bank;
import model.Customer;
import model.EventLog;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

// Constructs a user interface for the Bank Teller Application
public class BankTellerUI extends JFrame {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 800;
    private Bank bank;
    JDesktopPane desktop = new JDesktopPane();
    private final JInternalFrame controlPanel;
    private Customer currentCustomer;
    private JTextField customerName;
    private JTextField customerPhone;
    private Account currentAccount;
    private static final String JSON_STORE = "./data/myFile.json";
    private final JsonWriter jsonWriter = new JsonWriter(JSON_STORE);
    private final JsonReader jsonReader = new JsonReader(JSON_STORE);

    // Overall structure references https://github.students.cs.ubc.ca/CPSC210/AlarmSystem
    // MODIFIES: this
    // EFFECTS: initializes the bank object, creates a JDesktopPane and adds a mouse listener to it. Creates a
    //          JInternalFrame and positions and resizes it. Runs the addButtonMainPanel() method. Adds the
    //          JInternalFrame to the JDesktopPane.
    public BankTellerUI() {
        bank = new Bank();
        desktop.addMouseListener(new DesktopFocusAction());
        controlPanel = new JInternalFrame("Bank Options", true, true, true, true);
        controlPanel.setLayout(new BorderLayout());

        addButtonMainPanel();

        // when the control panel is closing, it will run the printLogEvents() method.
        controlPanel.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                printLogEvents();
            }
        });
        
        setContentPane(desktop);
        setTitle("Bank Teller Simulator");
        setSize(WIDTH, HEIGHT);

        controlPanel.pack();
        controlPanel.setVisible(true);

        int panelWidth = (WIDTH - controlPanel.getWidth()) / 2;
        int panelHeight = (HEIGHT - controlPanel.getHeight()) / 2;

        controlPanel.setLocation(panelWidth, panelHeight);
        desktop.add(controlPanel);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        centerOnScreen();
        setVisible(true);

    }


    // MODIFIES: this
    // EFFECTS: centers JDesktopPane on desktop window
    private void centerOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }

    // EFFECTS: positions customer's account menu and account options menu.
    private void positionOnScreenAccount(JFrame accountFrame) {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        accountFrame.setLocation((width - getWidth() + 200), (height - getHeight()));
    }


    // EFFECTS: positions customer's account options menu.
    private void positionOnScreenAccountOptions(JFrame accountFrame) {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        accountFrame.setLocation((width - getWidth() - 275), (height - getHeight()) - 50);
    }


    // EFFECTS: adds an image to a newly created JPanel, and then creates another JPanel to hold the buttons, and then
    //          adds the button JPanel to the JPanel holding the image, and then finally adds the combined JPanel to
    //          the main control panel, and then modifies the size and location of the control panel.
    private void addButtonMainPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());

        // Referenced https://www.dummies.com/article/technology/programming-web-design/java/how-to-write-java-code-
        // to-show-an-image-on-the-screen-150767/ to help with adding an image to a JPanel
        ImageIcon originalIcon = new ImageIcon("./data/pictures/bank_teller.jpg");

        // Referenced https://docs.oracle.com/javase/8/docs/api/javax/swing/ImageIcon.html and
        // referenced https://docs.oracle.com/javase%2F7%2Fdocs%2Fapi%2F%2F/java/awt/Image.html
        // to help rescale the image
        Image image = originalIcon.getImage().getScaledInstance(500,250, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(resizedIcon);
        buttonPanel.add(imageLabel, BorderLayout.NORTH);

        JPanel buttonSubPanel = new JPanel();
        buttonSubPanel.setLayout(new GridLayout(4,1));
        buttonSubPanel.add(new JButton(new AddCustomerAction()));
        buttonSubPanel.add(new JButton(new FindCustomerAction()));
        buttonSubPanel.add(new JButton(new LoadFileAction()));
        buttonSubPanel.add(new JButton(new SaveFileAction()));
        buttonPanel.add(buttonSubPanel, BorderLayout.CENTER);

        controlPanel.add(buttonPanel, BorderLayout.CENTER);
        buttonPanel.setPreferredSize(new Dimension(WIDTH - 300, HEIGHT - 300));
    }


    // EFFECTS: adds an image to a newly created JPanel, and then creates another JPanel to hold the buttons, and then
    //          adds the button JPanel to the JPanel holding the image. This combined JPanel is now added to a JFrame,
    //          and the JFrame's size and location is modified and the JFrame is set to be disposed once closed.
    private void addButtonAccountPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());

        ImageIcon originalIcon = new ImageIcon("./data/pictures/bank_teller2.jpg");
        Image image = originalIcon.getImage().getScaledInstance(500,250, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(resizedIcon);
        buttonPanel.add(imageLabel, BorderLayout.NORTH);


        JFrame accountOptionsFrame = new JFrame("Account Options");
        accountOptionsFrame.setLayout(new BorderLayout());
        JPanel buttonSubPanel = new JPanel();
        buttonSubPanel.setLayout(new GridLayout(4,2));
        buttonSubPanel.add(new JButton(new ShowAccountsAction()));
        buttonSubPanel.add(new JButton(new AddAccountsAction()));
        buttonSubPanel.add(new JButton(new RemoveAccountsAction()));
        buttonPanel.add(buttonSubPanel, BorderLayout.CENTER);

        accountOptionsFrame.add(buttonPanel, BorderLayout.CENTER);
        buttonPanel.setPreferredSize(new Dimension(WIDTH - 300, HEIGHT - 300));

        accountOptionsFrame.setSize(WIDTH - 300, HEIGHT - 300);
        positionOnScreenAccount(accountOptionsFrame);
        accountOptionsFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        accountOptionsFrame.setVisible(true);
    }


    // EFFECTS: Takes the three combined button & image panels, and adds them to a main JPanel,
    //          The main JPanel is then added to a JFrame, and the size and location of the JFrame is modified,
    //          and the JFrame is set to be disposed once closed.
    private void addButtonEachAccountPanel() {
        JFrame accountOptionsFrame = new JFrame("Account " + currentAccount.getAccountNumber());
        accountOptionsFrame.setLayout(new BorderLayout());

        JPanel buttonImagePanel1 = buttonImagePanel1();
        JPanel buttonImagePanel2 = buttonImagePanel2();
        JPanel buttonImagePanel3 = buttonImagePanel3();

        JPanel buttonMainPanel = new JPanel();
        buttonMainPanel.setLayout(new GridLayout(3,1));
        buttonMainPanel.add(buttonImagePanel1);
        buttonMainPanel.add(buttonImagePanel2);
        buttonMainPanel.add(buttonImagePanel3);

        accountOptionsFrame.add(buttonMainPanel, BorderLayout.CENTER);
        buttonMainPanel.setPreferredSize(new Dimension(400, HEIGHT));

        accountOptionsFrame.setSize(600, HEIGHT);
        positionOnScreenAccountOptions(accountOptionsFrame);
        accountOptionsFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        accountOptionsFrame.setVisible(true);

    }

    // EFFECTS: helper function that creates a JPanel with an image and a deposit button
    private JPanel buttonImagePanel1() {
        JPanel buttonImagePanel1 = new JPanel();
        buttonImagePanel1.setLayout(new BorderLayout());

        ImageIcon originalIcon1 = new ImageIcon("./data/pictures/account_deposit.png");
        Image image1 = originalIcon1.getImage().getScaledInstance(400,200, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon1 = new ImageIcon(image1);
        JLabel imageLabel1 = new JLabel(resizedIcon1);
        buttonImagePanel1.add(imageLabel1, BorderLayout.NORTH);

        JPanel buttonSubPanel1 = new JPanel();
        buttonSubPanel1.setLayout(new BorderLayout());
        buttonSubPanel1.add(new JButton(new DepositAccountAction()));
        buttonImagePanel1.add(buttonSubPanel1, BorderLayout.CENTER);

        return buttonImagePanel1;
    }

    // EFFECTS: helper function that creates a JPanel with an image and a withdrawal button
    private JPanel buttonImagePanel2() {
        JPanel buttonImagePanel2 = new JPanel();
        buttonImagePanel2.setLayout(new BorderLayout());

        ImageIcon originalIcon2 = new ImageIcon("./data/pictures/account_withdraw.png");
        Image image2 = originalIcon2.getImage().getScaledInstance(400,200, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon2 = new ImageIcon(image2);
        JLabel imageLabel2 = new JLabel(resizedIcon2);
        buttonImagePanel2.add(imageLabel2, BorderLayout.NORTH);

        JPanel buttonSubPanel2 = new JPanel();
        buttonSubPanel2.setLayout(new BorderLayout());
        buttonSubPanel2.add(new JButton(new WithdrawAccountAction()));
        buttonImagePanel2.add(buttonSubPanel2, BorderLayout.CENTER);

        return buttonImagePanel2;
    }

    // EFFECTS: helper function that creates a JPanel with an image and an account transactions button
    private JPanel buttonImagePanel3() {
        JPanel buttonImagePanel3 = new JPanel();
        buttonImagePanel3.setLayout(new BorderLayout());

        ImageIcon originalIcon3 = new ImageIcon("./data/pictures/account_transactions.jpg");
        Image image3 = originalIcon3.getImage().getScaledInstance(400,200, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon3 = new ImageIcon(image3);
        JLabel imageLabel3 = new JLabel(resizedIcon3);
        buttonImagePanel3.add(imageLabel3, BorderLayout.NORTH);

        JPanel buttonSubPanel3 = new JPanel();
        buttonSubPanel3.setLayout(new BorderLayout());
        buttonSubPanel3.add(new JButton(new ShowAccountTransactionAction()));
        buttonImagePanel3.add(buttonSubPanel3, BorderLayout.CENTER);

        return buttonImagePanel3;
    }


    // EFFECTS: Creates a JPanel button panel, and JFrame.
    //          if customer has accounts, then each of these accounts is added as a button to the JPanel, and the
    //          panel is added to the JFrame. Then the size of location of the JFrame is modified, and set to close
    //          if used.
    //          otherwise, a message is displayed indicating the customer has no accounts
    private void showAccountButtonPanel() {


        // for each customer account we create a button for it, and create button action for it as well,
        // so one it gets clicked it sets the correct account that was selected.
        if (!currentCustomer.getAccounts().isEmpty()) {
            JFrame accountFrame = new JFrame("Customer's Accounts");
            accountFrame.setLayout(new BorderLayout());

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(5, 2));

            for (Account account : currentCustomer.getAccounts()) {
                AccountButton accountButton = new AccountButton(account);
                accountButton.addActionListener(new AccountButtonAction(account, accountFrame));
                buttonPanel.add(accountButton);
            }
            accountFrame.add(buttonPanel, BorderLayout.CENTER);
            accountFrame.setSize(500, 350);
            positionOnScreenAccount(accountFrame);
            accountFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            accountFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(
                    BankTellerUI.this,
                    "Customer has no accounts open"
            );
        }
    }


    // REQUIRES: account != null and JFrame accountFrame != null
    // EFFECTS: creates an action for pressing the account button's in showAccountButtonPanel() method. When the button
    //          is pressed, it sets currentAccount field, to the account button pressed, and triggers the next menu, and
    //          closes the accountFrame.
    private class AccountButtonAction extends AbstractAction {
        private final Account account;
        private final JFrame accountFrame;

        public AccountButtonAction(Account account, JFrame accountFrame) {
            this.account = account;
            this.accountFrame = accountFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            currentAccount = account;
            addButtonEachAccountPanel();
            accountFrame.dispose();
        }
    }

    // EFFECTS: creates an action for pressing the add customer button in addButtonMainPanel() method. When the button
    //          is pressed, we create a JPanel that holds two text fields for user input.
    //          if the user presses ok, then we get that information, and add that customer to the bank, and update
    //          the currentCustomer field, and trigger the next menu.
    //          otherwise, we return to the previous menu.
    private class AddCustomerAction extends AbstractAction {

        AddCustomerAction() {
            super("Add Customer");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            //Referenced: https://docs.oracle.com/javase%2F7%2Fdocs%2Fapi%2F%2F/javax/swing/JTextField.html#:~:text=
            //JTextField%20is%20a%20lightweight%20component,awt. and https://www.geeksforgeeks.org/java-swing-jtextfield
            // to help understand how to use JTextField, and how to use it in a JPanel.

            JTextField nameTextField;
            JTextField phoneNumberTextField;
            JPanel inputPanel = new JPanel(new GridLayout(2,2));
            inputPanel.add(new JLabel("Name:"));
            nameTextField = new JTextField();
            inputPanel.add(nameTextField);

            inputPanel.add(new JLabel("Phone Number:"));
            phoneNumberTextField = new JTextField();
            inputPanel.add(phoneNumberTextField);

            customerName = nameTextField;
            customerPhone = phoneNumberTextField;

            int result = showConfirmDialogOk("Enter Customer Information", inputPanel);

            if (result == JOptionPane.OK_OPTION) {
                String name = customerName.getText();
                String phoneNumber = customerPhone.getText();
                Customer customer = new Customer(name, phoneNumber);
                bank.addACustomer(customer);
                currentCustomer = customer;
                addButtonAccountPanel();
            }
        }
    }

    // REQUIRES: text != null and inputPanel != null
    // EFFECTS: creates a pop up dialog message box (for confirming messages), with the inputs provided.
    // Referenced: https://docs.oracle.com/javase/8/docs/api/javax/swing/JOptionPane.html
    public int showConfirmDialogOk(String text, JPanel inputPanel) {
        return JOptionPane.showConfirmDialog(BankTellerUI.this, inputPanel,
                text,
                JOptionPane.OK_CANCEL_OPTION);
    }


    // EFFECTS: creates an action for pressing the find a customer button in addButtonMainPanel() method. When the
    //          button is pressed, we create a JPanel that holds two text fields for user input.
    //          if the user presses ok, then we get that information, and find the customer in the bank.
    //          if we find the customer then we update the currentCustomer field, and trigger the next menu.
    //          otherwise if no customer is found, then we display a pop-up error message.
    //          otherwise, if user presses cancel then we return to previous menu.
    private class FindCustomerAction extends AbstractAction {

        FindCustomerAction() {
            super("Find a Customer");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            boolean validInput = false;
            while (!validInput) {
                JPanel inputPanel = createInputPanel();

                int result = showConfirmDialogOk("Enter Customer Information", inputPanel);

                if (result == JOptionPane.OK_OPTION) {
                    Customer customer = bank.findACustomer(customerName.getText(), customerPhone.getText());

                    if (customer != null) {
                        currentCustomer = customer;
                        addButtonAccountPanel();
                        validInput = true;
                    } else {
                        showMessageErrorDialog("Incorrect information entered. Please try again.");
                    }
                } else {
                    return;
                }
            }
        }
    }

    // EFFECTS: creates a JPanel, that stores two JTextFields
    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(2,2));
        JTextField nameTextField;
        JTextField phoneNumberTextField;
        inputPanel.add(new JLabel("Name:"));
        nameTextField = new JTextField();
        inputPanel.add(nameTextField);

        inputPanel.add(new JLabel("Phone Number:"));
        phoneNumberTextField = new JTextField();
        inputPanel.add(phoneNumberTextField);

        customerName = nameTextField;
        customerPhone = phoneNumberTextField;

        return inputPanel;
    }

    // EFFECTS: creates an action for pressing the Deposit Money button in buttonImagePanel1() method. When the
    //          button is pressed, we create a JPanel that holds a text field for user input.
    //          if the amount is valid, then we deposit that amount in the account, and create a pop-up message dialog
    //          box.
    //          otherwise if amount is not valid, then we display a pop-up error message dialog box.
    //          otherwise, if user presses cancel then we display a pop-up dialog box indicating this.
    private class DepositAccountAction extends AbstractAction {

        DepositAccountAction() {
            super("Deposit Money");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            JPanel inputPanel = new JPanel(new GridLayout(2, 2));
            JTextField amountTextField;
            inputPanel.add(new JLabel("Amount:"));
            amountTextField = new JTextField();
            inputPanel.add(amountTextField);

            int result = showConfirmDialogOk("Enter the amount you want to deposit to the Account", inputPanel);

            if (result == JOptionPane.OK_OPTION && !amountTextField.getText().isEmpty()) {
                int amount = Integer.parseInt(amountTextField.getText());

                if (amount > 0) {
                    currentAccount.deposit(amount);
                    JOptionPane.showMessageDialog(BankTellerUI.this,
                            "Amount " + amount + " deposited into Account " + currentAccount.getAccountNumber()
                                    + " \n" + "Balance: " + currentAccount.getBalance()
                    );
                } else {
                    showMessageErrorDialog("Amount needs to be greater than zero!");
                }
            } else {
                JOptionPane.showMessageDialog(
                        BankTellerUI.this,
                        "Cancelled deposit!"
                );
            }
        }
    }

    // REQUIRES: text != null
    // EFFECTS: creates a error message dialog with the text provided
    public void showMessageErrorDialog(String text) {
        JOptionPane.showMessageDialog(BankTellerUI.this,
                text,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // REQUIRES: text != null
    // EFFECTS: creates a message dialog with the text provided
    public void showMessageDialog(String text) {
        JOptionPane.showMessageDialog(BankTellerUI.this, text);
    }

    // EFFECTS: creates an action for pressing the Withdraw Money button in buttonImagePanel2() method. When the
    //          button is pressed, we create a JPanel that holds a text field for user input.
    //          if the amount is valid, and have enough funds
    //          then we withdraw that amount in the account, and create a pop-up message dialog box indicating this.
    //          otherwise if account does not have enough funds, and create an error message dialog box.
    //          otherwise if amount is not valid, then we display a pop-up error message dialog box.
    //          otherwise, if user presses cancel then we display a pop-up dialog box indicating this.
    private class WithdrawAccountAction extends AbstractAction {

        WithdrawAccountAction() {
            super("Withdraw Money");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            JPanel inputPanel = new JPanel(new GridLayout(2, 2));
            JTextField amountTextField;
            inputPanel.add(new JLabel("Amount:"));
            amountTextField = new JTextField();
            inputPanel.add(amountTextField);

            int result = showConfirmDialogOk("Enter the amount you want to withdraw from the Account", inputPanel);

            if (result == JOptionPane.OK_OPTION && !amountTextField.getText().isEmpty()) {
                int amount = Integer.parseInt(amountTextField.getText());

                if (amount > 0) {
                    if (currentAccount.withdraw(amount)) {
                        showMessageDialog("Amount " + amount + " withdrawn from Account "
                                + currentAccount.getAccountNumber() + " \n" + "Balance: "
                                + currentAccount.getBalance());
                    } else {
                        showMessageErrorDialog("Insufficient Funds!, withdrawal cancelled!");
                    }
                } else {
                    showMessageErrorDialog("Amount needs to be greater than zero!");
                }
            } else {
                showMessageBasic("Cancelled withdraw!");
            }
        }
    }

    // REQUIRES: text != null
    // EFFECTS: creates a message dialog with the text provided
    public void showMessageBasic(String text) {
        JOptionPane.showMessageDialog(
                BankTellerUI.this,
                text
        );
    }

    // EFFECTS: creates an action for pressing the Load Bank File button in addButtonMainPanel() method. When the
    //          button is pressed, we create a confirm dialog message box.
    //          if user selects yes, then we run the bankLoadYesOption().
    //          otherwise, we create a message dialog box confirming it was not loaded;
    private class LoadFileAction extends AbstractAction {

        LoadFileAction() {
            super("Load Bank File");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            JPanel inputPanel = new JPanel(new GridLayout(2,2));

            int result = JOptionPane.showConfirmDialog(
                    BankTellerUI.this,
                    inputPanel,
                    "Do you want to load the bank file?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                bankLoadYesOption();
            } else {
                JOptionPane.showMessageDialog(
                        BankTellerUI.this,
                        "Did Not Load Bank State!"
                );
            }
        }
    }

    // EFFECTS: creates an action for pressing the Save Bank File button in addButtonMainPanel() method. When the
    //          button is pressed, we create a confirm dialog message box.
    //          if user selects yes, then we run bankSaveYesOption() method.
    //          otherwise, we create a message dialog box confirming it was not saved;
    private class SaveFileAction extends AbstractAction {

        SaveFileAction() {
            super("Save Bank File");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            JPanel inputPanel = new JPanel(new GridLayout(2,2));

            int result = JOptionPane.showConfirmDialog(
                    BankTellerUI.this,
                    inputPanel,
                    "Do you want to save the bank file",
                    JOptionPane.YES_NO_OPTION
            );

            if (result == JOptionPane.YES_OPTION) {
                bankSaveYesOption();
            } else {
                JOptionPane.showMessageDialog(
                        BankTellerUI.this,
                        "Did Not Save Bank State!"
                );
            }
        }
    }

    // EFFECTS: creates an action for pressing the Show Customer Accounts button in addButtonAccountPanel() method.
    //          When the button is pressed, we run the showAccountButtonPanel() method.
    private class ShowAccountsAction extends AbstractAction {

        ShowAccountsAction() {
            super("Show Customer " + currentCustomer.getName() + "'s Accounts");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            showAccountButtonPanel();

        }
    }

    // EFFECTS: creates an action for pressing the Add Account button in addButtonAccountPanel() method.
    //          When the button is pressed, we run the addAccount() method.
    private class AddAccountsAction extends AbstractAction {

        AddAccountsAction() {
            super("Add Account");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            addAccount();
        }
    }

    // EFFECTS: Creates a yes/no dialog box, if user selects yes, then calls accountYesOption()
    //          otherwise, creates an account with no balance, and displays a message dialog box indicating this.
    private void addAccount() {
        JPanel inputPanel = new JPanel(new GridLayout(2,2));
        inputPanel.add(new JLabel("Do you want to open this account with a balance?"));

        int result = JOptionPane.showConfirmDialog(
                BankTellerUI.this,
                inputPanel,
                " ",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            accountYesOption();
        } else {
            Account account = new Account(currentCustomer.getNextAccountNumber());
            currentCustomer.addAccount(account);
            JOptionPane.showMessageDialog(
                    BankTellerUI.this,
                    "Account " +  account.getAccountNumber() + " created with no balance"
            );
        }
    }

    // EFFECTS: creates an action for pressing the Remove Account button in addButtonAccountPanel() method.
    //          When the button is pressed, we run the removeAccount() method.
    private class RemoveAccountsAction extends AbstractAction {

        RemoveAccountsAction() {
            super("Remove Account");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            removeAccount();
        }
    }

    // EFFECTS: if the customer has accounts, then creates a JComboBox that stores the accounts of the customer, and
    //          adds it to a JPanel, and creates a confirm dialog box, so user can select the account they wish to
    //          remove.
    //          if the user selects a valid account and presses yes, then accountRemoveYesOption method is called.
    //          if the user presses no, a message is displayed saying no accounts are selected.
    //          otherwise, if customer has no accounts, a message dialog box is displayed indicating this.
    private void removeAccount() {
        if (!currentCustomer.getAccounts().isEmpty()) {
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(4,2));

            JComboBox<Account> accountComboBox = new JComboBox<>();

            // Adds account objects to combo box similar to alarm system example provided in Edx
            for (Account account : currentCustomer.getAccounts()) {
                accountComboBox.addItem(account);
            }

            buttonPanel.add(accountComboBox);

            int result = JOptionPane.showConfirmDialog(BankTellerUI.this, buttonPanel,
                    "Select the Account to Remove",
                    JOptionPane.YES_NO_OPTION
            );

            // Extracts the selected object from combo box similar to alarm system example provided in Edx
            Account selectedAccount = (Account) accountComboBox.getSelectedItem();

            if (result == JOptionPane.YES_OPTION && selectedAccount != null) {
                accountRemoveYesOption(selectedAccount);
            } else {
                showMessageErrorDialog("No Account Selected");
            }
        } else {
            showMessageErrorDialog("Customer has no accounts open");
        }

    }

    // EFFECTS: helper method that creates a new confirm dialog box to get a user input for an account balance.
    //          if a valid input is given, then a new account is created with the given balance.
    //          otherwise, just creates a new message dialog box, indicating no valid balance was given.
    private void accountYesOption() {
        JTextField balanceTextField;
        JPanel inputPanel2 = new JPanel(new GridLayout(2, 2));
        inputPanel2.add(new JLabel("Balance:"));
        balanceTextField = new JTextField();
        inputPanel2.add(balanceTextField);
        JOptionPane.showConfirmDialog(
                BankTellerUI.this,
                inputPanel2,
                "Account Initialization",
                JOptionPane.DEFAULT_OPTION
        );
        if (Objects.equals(balanceTextField.getText(), "") || Integer.parseInt(balanceTextField.getText()) < 0) {
            showMessageErrorDialog("Account was not created, no balance given!");
            return;
        }
        int balance = Integer.parseInt(balanceTextField.getText());
        Account account = new Account(currentCustomer.getNextAccountNumber(), balance);
        currentCustomer.addAccount(account);
        JOptionPane.showMessageDialog(
                BankTellerUI.this,
                "Account " + account.getAccountNumber() + " created with balance " + balance);
    }


    // EFFECTS: Reads data from the JSON file, and if successful, displays a message dialog box confirming this,
    //          otherwise, displays an error message dialog box, letting the user know it has failed to load file.
    private void bankLoadYesOption() {
        try {
            bank = jsonReader.read();
            JOptionPane.showMessageDialog(
                    BankTellerUI.this,
                    "Successfully Loaded Bank State"
            );
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    BankTellerUI.this,
                    "Unable to read from file: " + JSON_STORE,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // EFFECTS: creates a new confirm dialog box to get user input for the bank name, and then
    //          calls savesBank method, and confirms it to the user with a message dialog box
    private void bankSaveYesOption() {
        JTextField nameTextField;
        JPanel inputPanel2 = new JPanel(new GridLayout(2, 2));
        inputPanel2.add(new JLabel("Name:"));
        nameTextField = new JTextField();
        inputPanel2.add(nameTextField);
        JOptionPane.showConfirmDialog(
                BankTellerUI.this,
                inputPanel2,
                "Enter Bank's Name",
                JOptionPane.DEFAULT_OPTION
        );
        String name = nameTextField.getText();
        bank.setName(name);
        saveBank(bank);
        JOptionPane.showMessageDialog(
                BankTellerUI.this,
                "Saved " + bank.getName() + " to " + JSON_STORE
        );
    }

    // EFFECTS: Writes data to the JSON file if an error occurs
    // displays an error message dialog box, letting the user know it has failed to save the file.
    private void saveBank(Bank bank) {
        try {
            jsonWriter.open();
            jsonWriter.write(bank);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(
                    BankTellerUI.this,
                    "Unable to write to file: " + JSON_STORE,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // EFFECTS: helper method that removes an account for the customer if the balance is zero, and
    //          a message dialog box is displayed confirming the account is removed
    //          otherwise, a message dialog box is displayed to the user indicating the account can't be removed.
    private void accountRemoveYesOption(Account account) {
        int accNumber = account.getAccountNumber();
        if (account.getBalance() == 0) {
            currentCustomer.removeAccount(accNumber);
            JOptionPane.showMessageDialog(
                    BankTellerUI.this,
                    "Account " + accNumber + " removed successfully"
            );
        } else {
            JOptionPane.showMessageDialog(
                    BankTellerUI.this,
                    "Can't remove Account " + accNumber + " because balance is not zero",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }

    // EFFECTS: creates an action for pressing the Show Account Transactions button in buttonImagePanel3() method.
    //          When the button is pressed, if the account has no transactions, then a message dialog box is created
    //          indicating this.
    //          otherwise, each transaction is added to the string builder, and then displayed as a message dialog box.
    private class ShowAccountTransactionAction extends AbstractAction {

        ShowAccountTransactionAction() {
            super("Show Account Transactions");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            StringBuilder totalTransactions = new StringBuilder("Account Transactions \n");
            int i = 1;
            if (currentAccount.getTransactions().isEmpty()) {
                JOptionPane.showMessageDialog(
                        BankTellerUI.this,
                        "No transactions present on account!"
                );
            } else {
                for (String each : currentAccount.getTransactions()) {
                    totalTransactions.append(i).append(") ").append(each).append(" \n");
                    i++;
                }
                JOptionPane.showMessageDialog(
                        BankTellerUI.this,
                        totalTransactions.toString()
                );
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a new JFrame to the desktop pane, where the JFrame displays the contents in the EventLog, centers
    //          the JFrame relative to the width and height of the desktop pane.
    public void printLogEvents() {
        ScreenPrinter sp = new ScreenPrinter(this);
        desktop.add(sp);

        sp.printLog(EventLog.getInstance());
        int panelWidth = (WIDTH - sp.getWidth()) / 2;
        int panelHeight = (HEIGHT - sp.getHeight()) / 2;

        sp.setLocation(panelWidth, panelHeight);
    }


    // EFFECTS: Represents action to be taken when user clicks desktop to switch focus. (Needed for key handling.)
    private class DesktopFocusAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            BankTellerUI.this.requestFocusInWindow();
        }
    }

    // starts the application
    public static void main(String[] args) {
        new BankTellerUI();

    }
}


