package library.service;

import library.domain.Copy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

@Component
public class CopyService {

    /*
    You could argue whether or not storing copies in their own collection/table makes sense,
    of if you'd rather have them be a sub resource of Book entities. In my case I decided
    for having their own collection, because it's easier to reason about copies as their own
    entity when you get into the topic of loaning a copy.
     */
    private final HashMap<String, HashMap<String, Copy>> copies;

    private final BookService bookService;

    @Autowired
    public CopyService(final BookService bookService) {
        this.bookService = bookService;
        copies = new HashMap<>();
    }

    /*
    Here I'm opting to return an Optional.empty() if the Book is not found
    because I don't think an empty list would be correct as that might be misunderstood as
    "the book exists but has no copy", when we rather want to communicate that the book does not exist
    */
    public Optional<Collection<Copy>> getAllCopies(final String bookId) {
        final HashMap<String, Copy> copiesOfBook = copies.get(bookId);
        return Objects.nonNull(copiesOfBook) ? Optional.of(copiesOfBook.values()) : Optional.empty();
    }

    // Prefer to work with Optionals to avoid null checks everywhere
    public Optional<Copy> getCopy(final String bookId, final String id) {
        final HashMap<String, Copy> copiesOfBook = copies.get(bookId);
        return Objects.nonNull(copiesOfBook) ? Optional.ofNullable(copiesOfBook.get(id)) : Optional.empty();
    }

    /*
    Here I'm opting to return an Optional.empty() if the Book is not found,
    but you could argue that throwing an exception that is handled by the controller
    is more reasonable as that is probably to be considered "an exceptional case".
    */
    public Optional<Copy> createCopy(final Copy copy) {
        if (!bookService.getBook(copy.getBookId()).isPresent()) {
            return Optional.empty();
        }

        copies.putIfAbsent(copy.getBookId(), new HashMap<>());
        copies.get(copy.getBookId()).put(copy.getId(), copy);
        return Optional.of(copies.get(copy.getBookId()).get(copy.getId()));
    }
}
