package library.api;

import library.api.requests.PostBookRequest;
import library.domain.Book;
import library.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

import static library.domain.Book.bookBuilder;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    public BookController(final BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Book>> getAllBooks() {
        return new ResponseEntity<>(bookService.getAllBooks(), OK);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> getBook(@PathVariable final String id) {
        return bookService
                .getBook(id)
                .map(book -> new ResponseEntity<>(book, OK))
                .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> createBook(@RequestBody final PostBookRequest request) {
        return new ResponseEntity<>(
                bookService
                        .createBook(bookBuilder()
                                .withTitle(request.getTitle())
                                .withIsbn(request.getIsbn())
                                .build()),
                OK);
    }
}
