class LibraryWorker extends Thread {
    private LibraryManager manager;
    private String operation;
    private String bookId;
    private String userId;

    public LibraryWorker(LibraryManager manager, String operation, String bookId, String userId) {
        this.manager = manager;
        this.operation = operation;
        this.bookId = bookId;
        this.userId = userId;
    }

    @Override
    public void run() {
        try {
            System.out.println("Thread " + Thread.currentThread().getName() +
                    " attempting to " + operation + " book: " + bookId);

            if (operation.equals("ISSUE")) {
                manager.issueBook(bookId, userId);
            } else if (operation.equals("RETURN")) {
                manager.returnBook(bookId);
            }

            System.out.println("Thread " + Thread.currentThread().getName() +
                    " completed successfully!");

        } catch (BookNotAvailableException | InvalidBookException | OverdueException e) {
            System.out.println("Thread " + Thread.currentThread().getName() +
                    " failed: " + e.getMessage());
        }
    }
}