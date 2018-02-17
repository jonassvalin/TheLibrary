package library.service;

import library.domain.Book;
import library.domain.Copy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.Optional;

import static library.domain.Book.bookBuilder;
import static library.domain.Copy.copyBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CopyServiceTest {

    private CopyService copyService;

    @Mock
    private BookService bookService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        copyService = new CopyService(bookService);
    }

    /*
    Some tests in this class are a little contrived as they're basically testing the same methods over and over.
    Normally you wouldn't have the storage actually in the service, but rather in a separate repository,
    in which case you would instead verify that the repository was correctly called, and then the individual
    tests would be more unique.
     */
    @Test
    public void shouldGetAllCopiesOfBook() throws Exception {
        // given
        final Book book = bookBuilder()
                .withTitle("someTitle")
                .withIsbn("someIsbn")
                .build();
        when(bookService.getBook(book.getId())).thenReturn(Optional.of(book));
        final Object copyOne = copyService.createCopy(copyBuilder()
                .withBookId(book.getId())
                .build())
                .get();
        final Object copyTwo = copyService.createCopy(copyBuilder()
                .withBookId(book.getId())
                .build())
                .get();

        // when
        final Collection<Copy> allCopies = copyService.getAllCopiesOfBook(book.getId());

        // then
        assertThat(allCopies, containsInAnyOrder(copyOne, copyTwo));
    }

    @Test
    public void shouldReturnEmptyWhenGettingAllCopiesOfBookThatDoesNotExist() throws Exception {
        // given
        final String bookId = "someId";
        when(bookService.getBook(bookId)).thenReturn(Optional.empty());

        // when
        final Collection<Copy> allCopies = copyService.getAllCopiesOfBook(bookId);

        // then
        assertThat(allCopies, is(emptyCollectionOf(Copy.class)));
    }

    @Test
    public void shouldGetCopy() throws Exception {
        // given
        final Book book = bookBuilder()
                .withTitle("someTitle")
                .withIsbn("someIsbn")
                .build();
        final Copy copy = copyBuilder()
                .withBookId(book.getId())
                .build();
        when(bookService.getBook(book.getId())).thenReturn(Optional.of(book));
        copyService.createCopy(copy);

        // when
        final Optional<Copy> storedCopy = copyService.getCopy(copy.getId());

        // then
        assertThat(storedCopy, is(Optional.of(copy)));
        verify(bookService).getBook(book.getId());
    }

    @Test
    public void shouldCreateCopy() throws Exception {
        // given
        final Book book = bookBuilder()
                .withTitle("someTitle")
                .withIsbn("someIsbn")
                .build();
        final Copy copy = copyBuilder()
                .withBookId(book.getId())
                .build();
        when(bookService.getBook(book.getId())).thenReturn(Optional.of(book));

        // when
        final Optional<Copy> returnedCopy = copyService.createCopy(copy);

        // then
        assertThat(returnedCopy, is(Optional.of(copy)));
        assertThat(copyService.getCopy(copy.getId()), is(Optional.of(copy)));
        verify(bookService).getBook(book.getId());
    }

    @Test
    public void shouldNotAllowCopyToBeCreatedIfBookDoesNotExist() throws Exception {
        // given
        final Copy copy = copyBuilder()
                .withBookId("someBookIdThatDoesNotExist")
                .build();
        when(bookService.getBook(copy.getBookId())).thenReturn(Optional.empty());

        // when
        final Optional<Copy> storedCopy = copyService.createCopy(copy);

        // then
        assertThat(storedCopy, is(Optional.empty()));
        assertThat(copyService.getCopy(copy.getId()), is(Optional.empty()));
    }

}