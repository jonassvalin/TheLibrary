package library.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Copy {
    
    private final String id;

    private final String bookId;

    private final Instant createdAt;

    private Copy(final Copy.CopyBuilder builder) {
        Objects.requireNonNull(builder.bookId, "bookId must not be null");

        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.bookId = builder.bookId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Copy)) return false;
        final Copy that = (Copy) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, bookId);
    }

    public String getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getBookId() {
        return bookId;
    }

    public static Copy.CopyBuilder copyBuilder() {
        return new Copy.CopyBuilder();
    }

    public static class CopyBuilder {

        private String bookId;

        private CopyBuilder() {
        }

        public Copy.CopyBuilder withBookId(final String bookId) {
            this.bookId = bookId;
            return this;
        }

        public Copy build() {
            return new Copy(this);
        }
    }
}
