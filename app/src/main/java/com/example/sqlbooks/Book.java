package com.example.sqlbooks;

public class Book {
    private int _id;
    private String name;
    private String author;
    private String language;
    private int pages;
    private String short_desc;
    private String long_desc;
    private String image_url;
    private boolean isFavorite;

    public Book(int _id, String name, String author, String language, int pages, String short_desc, String long_desc, String image_url, boolean isFavorite) {
        this._id = _id;
        this.name = name;
        this.author = author;
        this.language = language;
        this.pages = pages;
        this.short_desc = short_desc;
        this.long_desc = long_desc;
        this.image_url = image_url;
        this.isFavorite = isFavorite;
    }

    public Book() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }

    public String getLong_desc() {
        return long_desc;
    }

    public void setLong_desc(String long_desc) {
        this.long_desc = long_desc;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public String toString() {
        return "Book{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", language='" + language + '\'' +
                ", pages=" + pages +
                ", short_desc='" + short_desc + '\'' +
                ", long_desc='" + long_desc + '\'' +
                ", image_url='" + image_url + '\'' +
                ", isFavorite=" + isFavorite +
                '}';
    }
}
