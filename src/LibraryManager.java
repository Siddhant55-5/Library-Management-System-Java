import java.io.*;
import java.util.*;

class LibraryManager {
    private List<LibraryItem> libraryItems;
    private List<Transaction> transactions;
    private FileHandler fileHandler;
    private int transactionCounter;

    public LibraryManager() {
        this.libraryItems = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.fileHandler = new FileHandler();
        this.transactionCounter = 1;
        loadData();
    }

    private void loadData() {
        try {
            libraryItems = fileHandler.loadBooks();
            transactions = fileHandler.loadTransactions();
            transactionCounter = transactions.size() + 1;
            System.out.println("Data loaded successfully!");
        } catch (IOException e) {
            System.out.println("No previous data found. Starting fresh.");
            fileHandler.logActivity("Error loading data: " + e.getMessage());
        }
    }

    private void saveData() {
        try {
            fileHandler.saveBooks(libraryItems);
            fileHandler.saveTransactions(transactions);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
            fileHandler.logActivity("Error saving data: " + e.getMessage());
        }
    }

    public synchronized void addBook(LibraryItem item) {
        libraryItems.add(item);
        saveData();
        fileHandler.logActivity("Added " + item.getType() + ": " + item.getTitle());
        System.out.println(item.getType() + " added successfully!");
    }

    public synchronized void issueBook(String bookId, String userId)
            throws BookNotAvailableException, InvalidBookException {
        LibraryItem item = findBookById(bookId);

        if (item == null) {
            throw new InvalidBookException("Book with ID " + bookId + " not found!");
        }

        if (!item.isAvailable()) {
            throw new BookNotAvailableException("Book is already issued!");
        }

        item.setAvailable(false);
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new java.util.Date());
        Transaction transaction = new Transaction("TXN" + transactionCounter++, bookId, userId, date);
        transactions.add(transaction);
        saveData();

        fileHandler.logActivity("Book issued: " + bookId + " to User: " + userId);
        System.out.println("Book issued successfully!");
        System.out.println(transaction);
    }

    public synchronized void returnBook(String bookId)
            throws InvalidBookException, OverdueException {
        LibraryItem item = findBookById(bookId);

        if (item == null) {
            throw new InvalidBookException("Book with ID " + bookId + " not found!");
        }

        if (item.isAvailable()) {
            throw new InvalidBookException("Book is not issued!");
        }

        // Find the transaction
        Transaction activeTransaction = null;
        for (Transaction t : transactions) {
            if (t.getBookId().equals(bookId) && t.getStatus().equals("ISSUED")) {
                activeTransaction = t;
                break;
            }
        }

        if (activeTransaction != null) {
            String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new java.util.Date());
            activeTransaction.setReturnDate(date);
            activeTransaction.setStatus("RETURNED");
        }

        item.setAvailable(true);
        saveData();
        fileHandler.logActivity("Book returned: " + bookId);
        System.out.println("Book returned successfully!");
    }

    public void searchBooks(String keyword) {
        System.out.println("\n--- Search Results ---");
        boolean found = false;
        for (LibraryItem item : libraryItems) {
            if (item.matchesSearch(keyword)) {
                System.out.println(item);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No books found matching: " + keyword);
        }
        fileHandler.logActivity("Search performed for: " + keyword);
    }

    public void displayAllBooks() {
        System.out.println("\n--- All Library Items ---");
        if (libraryItems.isEmpty()) {
            System.out.println("No items in library.");
            return;
        }
        for (LibraryItem item : libraryItems) {
            System.out.println(item);
        }
    }

    public void displayTransactions() {
        System.out.println("\n--- All Transactions ---");
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }

    public void generateReport() {
        try {
            fileHandler.generateReport(libraryItems, transactions);
        } catch (IOException e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }

    public void backupData() {
        try {
            fileHandler.backupData();
        } catch (IOException e) {
            System.out.println("Error creating backup: " + e.getMessage());
        }
    }

    private LibraryItem findBookById(String bookId) {
        for (LibraryItem item : libraryItems) {
            if (item.getId().equals(bookId)) {
                return item;
            }
        }
        return null;
    }
}