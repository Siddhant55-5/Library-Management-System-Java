class Transaction {
    private String transactionId;
    private String bookId;
    private String userId;
    private String issueDate;
    private String returnDate;
    private String status; // ISSUED, RETURNED, OVERDUE

    public Transaction(String transactionId, String bookId, String userId, String issueDate) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.userId = userId;
        this.issueDate = issueDate;
        this.returnDate = "Not Returned";
        this.status = "ISSUED";
    }

    public String getTransactionId() { return transactionId; }
    public String getBookId() { return bookId; }
    public String getUserId() { return userId; }
    public String getIssueDate() { return issueDate; }
    public String getReturnDate() { return returnDate; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
    public void setReturnDate(String date) { this.returnDate = date; }

    public String toFileString() {
        return transactionId + "," + bookId + "," + userId + "," +
                issueDate + "," + returnDate + "," + status;
    }

    @Override
    public String toString() {
        return String.format("Transaction ID: %s | Book ID: %s | User: %s | Issue Date: %s | Return Date: %s | Status: %s",
                transactionId, bookId, userId, issueDate, returnDate, status);
    }

    public static Transaction fromFileString(String line) {
        String[] parts = line.split(",");
        Transaction t = new Transaction(parts[0], parts[1], parts[2], parts[3]);
        t.setReturnDate(parts[4]);
        t.setStatus(parts[5]);
        return t;
    }
}