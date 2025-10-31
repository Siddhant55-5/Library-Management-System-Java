class Magazine extends LibraryItem {
    private String issueNumber;

    public Magazine(String id, String title, String author, String issueNumber) {
        super(id, title, author);
        this.issueNumber = issueNumber;
    }

    public String getIssueNumber() {
        return issueNumber;
    }

    @Override
    public String getType() {
        return "Magazine";
    }

    @Override
    public String toFileString() {
        return super.toFileString() + "," + issueNumber + ",MAGAZINE";
    }
}