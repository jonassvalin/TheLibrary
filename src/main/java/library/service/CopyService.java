package library.service;

import library.domain.Copy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CopyService {

    /*
    You could argue whether or not storing copies in their own collection/table makes sense,
    of if you'd rather have them be a sub resource of Book entities. In my case I decided
    for having their own collection, because it's easier to reason about copies as their own
    entity when you get into the topic of loaning a copy.
     */

    /**
     * Key: Id of the {@link library.domain.Copy}
     */
    private final HashMap<String, Copy> copies;

    private final BookService bookService;

    @Autowired
    public CopyService(final BookService bookService) {
        this.bookService = bookService;
        copies = new HashMap<>();
    }

    public Collection<Copy> getAllCopiesOfBook(final String bookId) {
        return copies.values()
                .stream()
                .filter(copy -> copy.getBookId().equals(bookId))
                .collect(Collectors.toList());
    }

    public Optional<Copy> getCopy(final String copyId) {
        return Optional.ofNullable(copies.get(copyId));
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

        copies.put(copy.getId(), copy);
        return Optional.of(copies.get(copy.getId()));
    }
}
