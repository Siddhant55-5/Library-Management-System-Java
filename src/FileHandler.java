import java.io.*;
import java.util.*;

class FileHandler {
    private static final String BOOKS_FILE = "library_books.txt";
    private static final String TRANSACTIONS_FILE = "library_transactions.txt";
    private static final String LOG_FILE = "library_log.txt";

    // Save all library items to file
    public synchronized void saveBooks(List<LibraryItem> items) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
            for (LibraryItem item : items) {
                writer.write(item.toFileString());
                writer.newLine();
            }
        }
        logActivity("Books data saved. Total items: " + items.size());
    }

    // Load all library items from file
    public synchronized List<LibraryItem> loadBooks() throws IOException {
        List<LibraryItem> items = new ArrayList<>();
        File file = new File(BOOKS_FILE);

        if (!file.exists()) {
            logActivity("No books file found. Starting with empty library.");
            return items;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String id = parts[0];
                    String title = parts[1];
                    String author = parts[2];
                    boolean isAvailable = Boolean.parseBoolean(parts[3]);
                    String extra = parts[4];
                    String type = parts[5];

                    LibraryItem item = null;
                    switch (type) {
                        case "BOOK":
                            item = new Book(id, title, author, extra);
                            break;
                        case "MAGAZINE":
                            item = new Magazine(id, title, author, extra);
                            break;
                        case "JOURNAL":
                            item = new Journal(id, title, author, extra);
                            break;
                    }

                    if (item != null) {
                        item.setAvailable(isAvailable);
                        items.add(item);
                    }
                }
            }
        }
        logActivity("Books loaded. Total items: " + items.size());
        return items;
    }

    // Save all transactions to file
    public synchronized void saveTransactions(List<Transaction> transactions) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE))) {
            for (Transaction t : transactions) {
                writer.write(t.toFileString());
                writer.newLine();
            }
        }
        logActivity("Transactions saved. Total: " + transactions.size());
    }

    // Load all transactions from file
    public synchronized List<Transaction> loadTransactions() throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(TRANSACTIONS_FILE);

        if (!file.exists()) {
            logActivity("No transactions file found. Starting fresh.");
            return transactions;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                transactions.add(Transaction.fromFileString(line));
            }
        }
        logActivity("Transactions loaded. Total: " + transactions.size());
        return transactions;
    }

    // Log system activities to a log file
    public synchronized void logActivity(String activity) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new java.util.Date());
            writer.write("[" + timestamp + "] " + activity);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }

    // Generate report and save to file
    public synchronized void generateReport(List<LibraryItem> items,
                                            List<Transaction> transactions) throws IOException {
        String reportFile = "library_report_" +
                new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()) + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reportFile))) {
            writer.write("========== LIBRARY REPORT ==========\n");
            writer.write("Generated: " + new java.util.Date() + "\n\n");

            writer.write("--- LIBRARY STATISTICS ---\n");
            writer.write("Total Items: " + items.size() + "\n");

            long availableCount = items.stream().filter(LibraryItem::isAvailable).count();
            writer.write("Available Items: " + availableCount + "\n");
            writer.write("Issued Items: " + (items.size() - availableCount) + "\n");
            writer.write("Total Transactions: " + transactions.size() + "\n\n");

            writer.write("--- ALL LIBRARY ITEMS ---\n");
            for (LibraryItem item : items) {
                writer.write(item.toString() + "\n");
            }

            writer.write("\n--- RECENT TRANSACTIONS ---\n");
            int recentCount = Math.min(10, transactions.size());
            for (int i = transactions.size() - recentCount; i < transactions.size(); i++) {
                writer.write(transactions.get(i).toString() + "\n");
            }

            writer.write("\n========== END OF REPORT ==========\n");
        }

        logActivity("Report generated: " + reportFile);
        System.out.println("Report saved as: " + reportFile);
    }

    // Backup data files
    public synchronized void backupData() throws IOException {
        String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new java.util.Date());

        // Backup books
        File booksFile = new File(BOOKS_FILE);
        if (booksFile.exists()) {
            copyFile(BOOKS_FILE, "backup_books_" + timestamp + ".txt");
        }

        // Backup transactions
        File transFile = new File(TRANSACTIONS_FILE);
        if (transFile.exists()) {
            copyFile(TRANSACTIONS_FILE, "backup_transactions_" + timestamp + ".txt");
        }

        logActivity("Backup completed: " + timestamp);
        System.out.println("Backup completed successfully!");
    }

    // Helper method to copy files
    private void copyFile(String source, String destination) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(source));
             BufferedWriter writer = new BufferedWriter(new FileWriter(destination))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}