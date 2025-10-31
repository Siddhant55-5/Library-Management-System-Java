class Book extends LibraryItem {
    private String isbn;

    public Book(String id, String title, String author, String isbn) {
        super(id, title, author);
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    @Override
    public String getType() {
        return "Book";
    }

    @Override
    public String toFileString() {
        return super.toFileString() + "," + isbn + ",BOOK";
    }
}