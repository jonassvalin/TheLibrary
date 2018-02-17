package library.api;

import library.api.requests.PostBookRequest;
import library.domain.Book;
import library.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static library.domain.Book.bookBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

public class BookControllerTest {

    private BookController bookController;

    @Mock
    private BookService bookService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        bookController = new BookController(bookService);
    }

    @Test
    public void shouldGetBook() throws Exception {
        // given
        final String id = "someId";
        final Book book = bookBuilder()
                .withTitle("someTitle")
                .withIsbn("someIsbn")
                .build();

        when(bookService.getBook(id)).thenReturn(Optional.of(book));

        // when
        final ResponseEntity<Book> response = bookController.getBook(id);

        // then
        final ResponseEntity<Book> expectedResponse = new ResponseEntity<>(book, OK);
        assertThat(response, is(expectedResponse));
        verify(bookService).getBook(id);
    }

    @Test
    public void shouldReturnNotFound() throws Exception {
        // given
        final String id = "someId";
        when(bookService.getBook(id)).thenReturn(Optional.empty());

        // when
        final ResponseEntity<Book> response = bookController.getBook(id);

        // then
        final ResponseEntity<Book> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        assertThat(response, is(expectedResponse));
        verify(bookService).getBook(id);
    }

    @Test
    public void shouldGetAllBooks() throws Exception {
        // given
        final Book book = bookBuilder()
                .withTitle("someTitle")
                .withIsbn("someIsbn")
                .build();
        when(bookService.getAllBooks()).thenReturn(singletonList(book));

        // when
        final ResponseEntity<Collection<Book>> response = bookController.getAllBooks();

        // then
        final ResponseEntity<Collection<Book>> expectedResponse = new ResponseEntity<>(singletonList(book), OK);
        assertThat(response, is(expectedResponse));
        verify(bookService).getAllBooks();
    }

    @Test
    public void shouldCreateBook() throws Exception {
        // given
        final String title = "someTitle";
        final String isbn = "someIsbn";
        when(bookService.createBook(any())).thenAnswer(methodCall -> methodCall.getArgumentAt(0, Book.class));

        // when
        final ResponseEntity<Book> response = bookController.createBook(new PostBookRequest(title, isbn));

        // then
        /*
        Normally you would probably have some sort of converter class to convert between the Request and the Book,
        in which case you would verify that the call to the converter was correctly performed and that the Controller
        returned the value Book object returned by the converter. In this case, since we're doing the conversion
        directly in the Controller (keeping within the time constraints of the assignment) I just want to check
        that the Book object was created with the correct fields from the request
         */
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().getTitle(), is(title));
        assertThat(response.getBody().getIsbn(), is(isbn));
        verify(bookService).createBook(any());
    }

}