import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class User {
    private String name;
    private String pin;
    private double balance;
    private ArrayList<String> transactionHistory;

    public User(String name, String pin, double balance) {
        this.name = name;
        this.pin = pin;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public boolean withdraw(double amount) {
        if (amount > balance || amount <= 0) {
            return false;
        }
        balance -= amount;
        addTransaction("Withdrew: $" + amount);
        return true;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            addTransaction("Deposited: $" + amount);
        }
    }

    public void changePin(String newPin) {
        this.pin = newPin;
    }

    public void viewTransactionHistory() {
        printStarBorder();
        System.out.println("* Transaction History:");
        if (transactionHistory.isEmpty()) {
            System.out.println("* No transactions found.");
        } else {
            for (String transaction : transactionHistory) {
                System.out.println("* " + transaction);
            }
        }
        printStarBorder();
    }

    private void addTransaction(String transaction) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        transactionHistory.add(transaction + " on " + timestamp);
    }

    private void printStarBorder() {
        String border = "*".repeat(60);
        System.out.println(border);
    }
}

public class ATM {
    private HashMap<String, User> users;
    private User currentUser;
    private static final int MAX_ATTEMPTS = 3;
    private static final double DAILY_WITHDRAWAL_LIMIT = 1000.0;

    public ATM() {
        users = new HashMap<>();
        // Sample users for testing
        users.put("1234", new User("Abul Kalam", "1234", 1000.0));
        users.put("5678", new User("Fatema Begum", "5678", 2000.0));
        users.put("4321", new User("Rafiq Ahmed", "4321", 1500.0));
        users.put("8765", new User("Shahina Akter", "8765", 1800.0));
        users.put("1122", new User("Kamal Hossain", "1122", 2500.0));
        users.put("2211", new User("Jahanara Khatun", "2211", 1200.0));
        users.put("3344", new User("Tariq Mahmood", "3344", 3000.0));
    }

    public void start() {
        welcomeMessage();
        Scanner scanner = new Scanner(System.in);
        int attempts = 0;
        while (true) {
            printStarBorder();
            System.out.println("* Welcome to the ATM");
            System.out.print("* Enter your PIN: ");
            String pin = scanner.nextLine();

            if (authenticate(pin)) {
                System.out.println("* Login successful!");
                System.out.println("* Welcome, " + currentUser.getName() + "!");
                showMenu(scanner);
                break;
            } else {
                System.out.println("* Invalid PIN. Please try again.");
                attempts++;
                if (attempts >= MAX_ATTEMPTS) {
                    System.out.println("* Too many incorrect attempts. Account locked.");
                    return;
                }
            }
        }
    }

    private boolean authenticate(String pin) {
        currentUser = users.get(pin);
        return currentUser != null;
    }

    private void showMenu(Scanner scanner) {
        while (true) {
            printStarBorder();
            System.out.println("* ATM Menu:");
            System.out.println("* 1. View Balance");
            System.out.println("* 2. Withdraw Funds");
            System.out.println("* 3. Deposit Funds");
            System.out.println("* 4. Change PIN");
            System.out.println("* 5. View Transaction History");
            System.out.println("* 6. Register New User");
            System.out.println("* 7. Exit");
            System.out.print("* Select an option: ");

            int choice = 0;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } else {
                System.out.println("* Invalid input. Please enter a number.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("* Current Balance: $" + currentUser.getBalance());
                    break;
                case 2:
                    handleWithdrawal(scanner);
                    break;
                case 3:
                    handleDeposit(scanner);
                    break;
                case 4:
                    handleChangePin(scanner);
                    break;
                case 5:
                    currentUser.viewTransactionHistory();
                    break;
                case 6:
                    registerNewUser(scanner);
                    break;
                case 7:
                    System.out.println("* Thank you for using the ATM. Goodbye!");
                    return;
                default:
                    System.out.println("* Invalid option. Please try again.");
            }
        }
    }

    private void handleWithdrawal(Scanner scanner) {
        System.out.print("* Enter amount to withdraw: $");
        if (scanner.hasNextDouble()) {
            double withdrawAmount = scanner.nextDouble();
            if (withdrawAmount > 0 && withdrawAmount <= DAILY_WITHDRAWAL_LIMIT) {
                if (currentUser.withdraw(withdrawAmount)) {
                    System.out.println("* Withdrawal successful. New Balance: $" + currentUser.getBalance());
                } else {
                    System.out.println("* Insufficient balance.");
                }
            } else {
                System.out.println("* Invalid amount or exceeds daily limit ($" + DAILY_WITHDRAWAL_LIMIT + ").");
            }
        } else {
            System.out.println("* Invalid input. Please enter a valid amount.");
            scanner.nextLine();
        }
    }

    private void handleDeposit(Scanner scanner) {
        System.out.print("* Enter amount to deposit: $");
        if (scanner.hasNextDouble()) {
            double depositAmount = scanner.nextDouble();
            if (depositAmount > 0) {
                currentUser.deposit(depositAmount);
                System.out.println("* Deposit successful. New Balance: $" + currentUser.getBalance());
            } else {
                System.out.println("* Invalid amount.");
            }
        } else {
            System.out.println("* Invalid input. Please enter a valid amount.");
            scanner.nextLine();
        }
    }

    private void handleChangePin(Scanner scanner) {
        System.out.print("* Enter new PIN: ");
        String newPin = scanner.nextLine();
        currentUser.changePin(newPin);
        System.out.println("* PIN changed successfully.");
    }

    private void registerNewUser(Scanner scanner) {
        System.out.print("* Enter new user name: ");
        String name = scanner.nextLine();
        System.out.print("* Enter new PIN: ");
        String pin = scanner.nextLine();
        System.out.print("* Enter initial deposit: $");
        if (scanner.hasNextDouble()) {
            double initialDeposit = scanner.nextDouble();
            if (initialDeposit >= 0) {
                users.put(pin, new User(name, pin, initialDeposit));
                System.out.println("* User registered successfully!");
            } else {
                System.out.println("* Initial deposit must be non-negative.");
            }
        } else {
            System.out.println("* Invalid input. Please try again.");
            scanner.nextLine();
        }
    }

    public static void welcomeMessage() {
        printStarBorder();
        System.out.println("* Welcome to the ATM System");
        System.out.println("* Your Secure Banking Experience");
        printStarBorder();
    }

    private static void printStarBorder() {
        String border = "*".repeat(60);
        System.out.println(border);
    }

    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.start();
    }
}
