package library.service;

import library.domain.Book;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Optional;

import static library.domain.Book.bookBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class BookServiceTest {
    private BookService bookService;

    @Before
    public void setUp() throws Exception {
        bookService = new BookService();
    }

    /*
    The tests in this class are a little contrived as they're basically testing the same methods over and over.
    Normally you wouldn't have the storage actually in the service, but rather in a separate repository,
    in which case you would instead verify that the repository was correctly called, and then the individual
    tests would be more unique.
     */

    @Test
    public void shouldGetAllBooks() throws Exception {
        // given
        final Book someBook = bookService.createBook(bookBuilder()
                .withTitle("someTitle")
                .withIsbn("someIsbn")
                .build());
        final Book someOtherBook = bookService.createBook(bookBuilder()
                .withTitle("someOtherTitle")
                .withIsbn("someOtherIsbn")
                .build());

        // when
        final Collection<Book> allBooks = bookService.getAllBooks();

        // then
        assertThat(allBooks, containsInAnyOrder(someBook, someOtherBook));
    }

    @Test
    public void shouldCreateBook() throws Exception {
        // given
        final Book book = bookBuilder()
                .withTitle("someTitle")
                .withIsbn("someIsbn")
                .build();

        // when
        bookService.createBook(book);

        // then
        final Optional<Book> storedBook = bookService.getBook(book.getId());
        assertThat(storedBook, is(Optional.of(book)));
    }

    @Test
    public void shouldGetBook() throws Exception {
        // given
        final Book book = bookService.createBook(bookBuilder()
                .withTitle("someTitle")
                .withIsbn("someIsbn")
                .build());

        // when
        final Optional<Book> storedBook = bookService.getBook(book.getId());

        // then
        assertThat(storedBook, is(Optional.of(book)));
    }

}