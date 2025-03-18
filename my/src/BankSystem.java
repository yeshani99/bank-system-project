import java.io.*;
import java.util.*;

class BankAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    private String accountHolder;
    private int accountNumber;
    private double balance;

    public BankAccount(String accountHolder, int accountNumber, double balance) {
        this.accountHolder = accountHolder;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    // Deposit Money
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposit Successful! New Balance: $" + balance);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    // Withdraw Money
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawal Successful! New Balance: $" + balance);
        } else {
            System.out.println("Insufficient funds or invalid amount.");
        }
    }

    // Check Balance
    public void checkBalance() {
        System.out.println("Account Number: " + accountNumber + " | Balance: $" + balance);
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String toString() {
        return "Account Holder: " + accountHolder + ", Account Number: " + accountNumber + ", Balance: $" + balance;
    }
}

public class BankSystem {
    private static final String FILE_NAME = "accounts.dat";
    private static Map<Integer, BankAccount> accounts = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadAccounts(); // Load existing accounts from file
        while (true) {
            System.out.println("\n🏦 Bank Account System 🏦");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. Display All Accounts");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> createAccount();
                case 2 -> depositMoney();
                case 3 -> withdrawMoney();
                case 4 -> checkBalance();
                case 5 -> displayAllAccounts();
                case 6 -> {
                    saveAccounts(); // Save accounts to file before exiting
                    System.out.println("Exiting... Thank you for using our system!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    // Method to create a new bank account
    private static void createAccount() {
        System.out.print("Enter Account Holder's Name: ");
        scanner.nextLine(); // Consume the newline
        String name = scanner.nextLine();
        System.out.print("Enter Initial Deposit: ");
        double initialBalance = scanner.nextDouble();

        int accountNumber = generateAccountNumber();
        BankAccount newAccount = new BankAccount(name, accountNumber, initialBalance);
        accounts.put(accountNumber, newAccount);
        System.out.println("✅ Account Created! Account Number: " + accountNumber);
    }

    // Method to deposit money
    private static void depositMoney() {
        System.out.print("Enter Account Number: ");
        int accountNumber = scanner.nextInt();
        if (accounts.containsKey(accountNumber)) {
            System.out.print("Enter Amount to Deposit: ");
            double amount = scanner.nextDouble();
            accounts.get(accountNumber).deposit(amount);
        } else {
            System.out.println("❌ Account not found!");
        }
    }

    // Method to withdraw money
    private static void withdrawMoney() {
        System.out.print("Enter Account Number: ");
        int accountNumber = scanner.nextInt();
        if (accounts.containsKey(accountNumber)) {
            System.out.print("Enter Amount to Withdraw: ");
            double amount = scanner.nextDouble();
            accounts.get(accountNumber).withdraw(amount);
        } else {
            System.out.println("❌ Account not found!");
        }
    }

    // Method to check balance
    private static void checkBalance() {
        System.out.print("Enter Account Number: ");
        int accountNumber = scanner.nextInt();
        if (accounts.containsKey(accountNumber)) {
            accounts.get(accountNumber).checkBalance();
        } else {
            System.out.println("❌ Account not found!");
        }
    }

    // Method to display all accounts
    private static void displayAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("📭 No accounts available.");
        } else {
            System.out.println("📋 All Bank Accounts:");
            for (BankAccount account : accounts.values()) {
                System.out.println(account);
            }
        }
    }

    // Generate unique account number
    private static int generateAccountNumber() {
        Random random = new Random();
        int accountNumber;
        do {
            accountNumber = 10000 + random.nextInt(90000); // 5-digit random number
        } while (accounts.containsKey(accountNumber));
        return accountNumber;
    }

    // Save accounts to file
    private static void saveAccounts() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(accounts);
            System.out.println("💾 Accounts saved successfully.");
        } catch (IOException e) {
            System.out.println("❌ Error saving accounts.");
            e.printStackTrace();
        }
    }

    // Load accounts from file
    @SuppressWarnings("unchecked")
    private static void loadAccounts() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            accounts = (Map<Integer, BankAccount>) in.readObject();
            System.out.println("📂 Accounts loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("🔍 No previous accounts found. Starting fresh.");
        }
    }
}
