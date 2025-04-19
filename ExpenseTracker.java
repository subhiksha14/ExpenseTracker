import java.sql.*;
import java.util.Scanner;

public class ExpenseTracker {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/expense_tracker";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "subhiksha@14"; // CHANGE THIS to your MySQL password

    public static void main(String[] args) {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.jdbc.Driver");


            // Establish the connection
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\n=== Expense Tracker ===");
                System.out.println("1. Add Expense");
                System.out.println("2. View All Expenses");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        addExpense(conn, scanner);
                        break;
                    case 2:
                        viewExpenses(conn);
                        break;
                    case 3:
                        System.out.println("Exiting... Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }

    private static void addExpense(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Description: ");
        String desc = scanner.nextLine();

        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Category: ");
        String category = scanner.nextLine();

        System.out.print("Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        String sql = "INSERT INTO expenses (description, amount, category, date) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, desc);
        stmt.setDouble(2, amount);
        stmt.setString(3, category);
        stmt.setDate(4, Date.valueOf(date));

        int rows = stmt.executeUpdate();
        if (rows > 0) {
            System.out.println("Expense added successfully.");
        } else {
            System.out.println("Failed to add expense.");
        }
    }

    private static void viewExpenses(Connection conn) throws SQLException {
        String sql = "SELECT * FROM expenses";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("\nID | Description | Amount | Category | Date");
        System.out.println("--------------------------------------------------");

        while (rs.next()) {
            System.out.printf("%d | %s | ₹%.2f | %s | %s\n",
                    rs.getInt("id"),
                    rs.getString("description"),
                    rs.getDouble("amount"),
                    rs.getString("category"),
                    rs.getDate("date"));
        }
    }
}
