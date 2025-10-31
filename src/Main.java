import java.util.Scanner;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        LibraryManager manager = new LibraryManager();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Library Management System!");
        System.out.println("All operations are logged and data is persisted.\n");

        while (true) {
            System.out.println("\n========== LIBRARY MANAGEMENT SYSTEM ==========");
            System.out.println("1.  Add Book");
            System.out.println("2.  Add Magazine");
            System.out.println("3.  Add Journal");
            System.out.println("4.  Issue Book");
            System.out.println("5.  Return Book");
            System.out.println("6.  Search Books");
            System.out.println("7.  Display All Items");
            System.out.println("8.  Display Transactions");
            System.out.println("9.  Generate Report (File)");
            System.out.println("10. Backup Data");
            System.out.println("11. Test Concurrent Issue (Multi-threading)");
            System.out.println("0.  Exit");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine();
                continue;
            }

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter Book ID: ");
                        String bookId = scanner.nextLine();
                        System.out.print("Enter Title: ");
                        String title = scanner.nextLine();
                        System.out.print("Enter Author: ");
                        String author = scanner.nextLine();
                        System.out.print("Enter ISBN: ");
                        String isbn = scanner.nextLine();
                        manager.addBook(new Book(bookId, title, author, isbn));
                        break;

                    case 2:
                        System.out.print("Enter Magazine ID: ");
                        String magId = scanner.nextLine();
                        System.out.print("Enter Title: ");
                        String magTitle = scanner.nextLine();
                        System.out.print("Enter Publisher: ");
                        String publisher = scanner.nextLine();
                        System.out.print("Enter Issue Number: ");
                        String issueNum = scanner.nextLine();
                        manager.addBook(new Magazine(magId, magTitle, publisher, issueNum));
                        break;

                    case 3:
                        System.out.print("Enter Journal ID: ");
                        String journalId = scanner.nextLine();
                        System.out.print("Enter Title: ");
                        String journalTitle = scanner.nextLine();
                        System.out.print("Enter Editor: ");
                        String editor = scanner.nextLine();
                        System.out.print("Enter Volume: ");
                        String volume = scanner.nextLine();
                        manager.addBook(new Journal(journalId, journalTitle, editor, volume));
                        break;

                    case 4:
                        System.out.print("Enter Book ID to issue: ");
                        String issueBookId = scanner.nextLine();
                        System.out.print("Enter User ID: ");
                        String userId = scanner.nextLine();
                        manager.issueBook(issueBookId, userId);
                        break;

                    case 5:
                        System.out.print("Enter Book ID to return: ");
                        String returnBookId = scanner.nextLine();
                        manager.returnBook(returnBookId);
                        break;

                    case 6:
                        System.out.print("Enter search keyword: ");
                        String keyword = scanner.nextLine();
                        manager.searchBooks(keyword);
                        break;

                    case 7:
                        manager.displayAllBooks();
                        break;

                    case 8:
                        manager.displayTransactions();
                        break;

                    case 9:
                        System.out.println("Generating report...");
                        manager.generateReport();
                        break;

                    case 10:
                        System.out.println("Creating backup...");
                        manager.backupData();
                        break;

                    case 11:
                        System.out.println("\n--- Testing Concurrent Book Issuing ---");
                        System.out.println("Two threads will try to issue the same book simultaneously.");
                        System.out.print("Enter Book ID for concurrent test: ");
                        String testBookId = scanner.nextLine();

                        LibraryWorker worker1 = new LibraryWorker(manager, "ISSUE", testBookId, "User1");
                        LibraryWorker worker2 = new LibraryWorker(manager, "ISSUE", testBookId, "User2");

                        worker1.start();
                        worker2.start();

                        worker1.join();
                        worker2.join();

                        System.out.println("\nConcurrent test completed!");
                        System.out.println("Only one thread should have succeeded due to synchronization.");
                        break;

                    case 0:
                        System.out.println("\nThank you for using Library Management System!");
                        System.out.println("All data has been saved.");
                        scanner.close();
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            } catch (BookNotAvailableException | InvalidBookException | OverdueException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.

    }
}