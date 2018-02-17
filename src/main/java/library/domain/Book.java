package library.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Book {

    private final String id;

    private final String title;

    private final String isbn;

    private final Instant createdAt;

    private Book(final Book.BookBuilder builder) {
        Objects.requireNonNull(builder.title, "title must not be null");
        Objects.requireNonNull(builder.isbn, "isbn must not be null");

        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.title = builder.title;
        this.isbn = builder.isbn;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        final Book that = (Book) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(title, that.title) &&
                Objects.equals(isbn, that.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, title, isbn);
    }

    public String getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public static Book.BookBuilder bookBuilder() {
        return new Book.BookBuilder();
    }

    public static class BookBuilder {

        private String title;

        private String isbn;

        private BookBuilder() {
        }

        public Book.BookBuilder withTitle(final String title) {
            this.title = title;
            return this;
        }

        public Book.BookBuilder withIsbn(final String isbn) {
            this.isbn = isbn;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }
}
