package org.reactive.part1.model;

import java.util.Objects;

public class Book {

    public static final Book BOOK1 = new Book("Silent Patient", "Alex Michaelides", 2019);
    public static final Book BOOK2 = new Book("Dune", "Frank Herbert", 1982);
    public static final Book BOOK3 = new Book("Nineteen Eighty-Four", "George Orwell", 1953);
    public static final Book BOOK4 = new Book("sgoodman", "Saul", 2020);

    private final String title;

    private final String author;

    private final Integer year;

    public Book(String title, String author, Integer year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Book book = (Book) o;
        return Objects.equals(year, book.year) &&
            Objects.equals(title, book.title) &&
            Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, year);
    }

    @Override
    public String toString() {
        return "Book{" +
            "title='" + title + '\'' +
            ", author='" + author + '\'' +
            ", year=" + year +
            '}';
    }
}
