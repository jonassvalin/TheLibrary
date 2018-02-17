package library.service;

import library.domain.Book;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Component
public class BookService {

    /**
     * Key: Id of the {@link library.domain.Book}
     */
    private final HashMap<String, Book> books;

    public BookService() {
        books = new HashMap<>();
    }

    public Collection<Book> getAllBooks() {
        return books.values();
    }

    // Prefer to work with Optionals to avoid needing null checks
    public Optional<Book> getBook(final String id) {
        return Optional.ofNullable(books.get(id));
    }

    /*
    This implementation is a bit odd, but I wanted to mimic the behaviour of a database call
    that would return the newly created object rather than a boolean or such,
    but HashMap.put returns the old value (or null), not the newly created value, hence the get call.
    */
    public Book createBook(final Book book) {
        books.put(book.getId(), book);
        return books.get(book.getId());
    }

}
