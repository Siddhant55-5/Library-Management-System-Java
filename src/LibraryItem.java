import java.io.*;

abstract class LibraryItem implements Searchable {
    protected String id;
    protected String title;
    protected String author;
    protected boolean isAvailable;

    public LibraryItem(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    public abstract String getType();

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { this.isAvailable = available; }

    @Override
    public boolean matchesSearch(String keyword) {
        keyword = keyword.toLowerCase();
        return title.toLowerCase().contains(keyword) ||
                author.toLowerCase().contains(keyword) ||
                id.toLowerCase().contains(keyword);
    }

    public String toFileString() {
        return id + "," + title + "," + author + "," + isAvailable;
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Title: %s | Author: %s | Type: %s | Status: %s",
                id, title, author, getType(), isAvailable ? "Available" : "Issued");
    }
}