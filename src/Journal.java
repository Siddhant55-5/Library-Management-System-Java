class Journal extends LibraryItem {
    private String volume;

    public Journal(String id, String title, String author, String volume) {
        super(id, title, author);
        this.volume = volume;
    }

    public String getVolume() {
        return volume;
    }

    @Override
    public String getType() {
        return "Journal";
    }

    @Override
    public String toFileString() {
        return super.toFileString() + "," + volume + ",JOURNAL";
    }
}