package library.api;

import library.domain.Copy;
import library.service.CopyService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static library.domain.Copy.copyBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

public class CopyControllerTest {

    private CopyController copyController;

    @Mock
    private CopyService copyService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        copyController = new CopyController(copyService);
    }

    @Test
    public void shouldGetCopy() throws Exception {
        // given
        final String bookId = "someBookId";
        final String id = "someId";
        final Copy copy = copyBuilder()
                .withBookId(bookId)
                .build();

        when(copyService.getCopy(bookId, id)).thenReturn(Optional.of(copy));

        // when
        final ResponseEntity<Copy> response = copyController.getCopy(bookId, id);

        // then
        final ResponseEntity<Copy> expectedResponse = new ResponseEntity<>(copy, OK);
        assertThat(response, is(expectedResponse));
        verify(copyService).getCopy(bookId, id);
    }

    @Test
    public void shouldReturnNotFound() throws Exception {
        // given
        final String bookId = "someBookId";
        final String id = "someId";
        when(copyService.getCopy(bookId, id)).thenReturn(Optional.empty());

        // when
        final ResponseEntity<Copy> response = copyController.getCopy(bookId, id);

        // then
        final ResponseEntity<Copy> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        assertThat(response, is(expectedResponse));
        verify(copyService).getCopy(bookId, id);
    }

    @Test
    public void shouldGetAllCopies() throws Exception {
        // given
        final String bookId = "someBookId";
        final Copy copy = copyBuilder()
                .withBookId(bookId)
                .build();
        when(copyService.getAllCopies(bookId)).thenReturn(Optional.of(singletonList(copy)));

        // when
        final ResponseEntity<Collection<Copy>> response = copyController.getAllCopies(bookId);

        // then
        final ResponseEntity<Collection<Copy>> expectedResponse = new ResponseEntity<>(singletonList(copy), OK);
        assertThat(response, is(expectedResponse));
        verify(copyService).getAllCopies(bookId);
    }

    @Test
    public void shouldReturnNotFoundIfBookDoesNotExist() throws Exception {
        // given
        final String bookId = "someBookId";
        when(copyService.getAllCopies(bookId)).thenReturn(Optional.empty());

        // when
        final ResponseEntity<Collection<Copy>> response = copyController.getAllCopies(bookId);

        // then
        final ResponseEntity<Collection<Copy>> expectedResponse = new ResponseEntity<>(NOT_FOUND);
        assertThat(response, is(expectedResponse));
        verify(copyService).getAllCopies(bookId);
    }

    @Test
    public void shouldCreateCopy() throws Exception {
        // given
        final String bookId = "someBookId";
        when(copyService.createCopy(any())).thenAnswer(methodCall -> Optional.of(methodCall.getArgumentAt(0, Copy.class)));

        // when
        final ResponseEntity<Copy> response = copyController.createCopy(bookId);

        // then
        /*
        Normally you would probably have some sort of converter class to convert between the Request and the Copy,
        in which case you would verify that the call to the converter was correctly performed and that the Controller
        returned the value Copy object returned by the converter. In this case, since we're doing the conversion
        directly in the Controller (keeping within the time constraints of the assignment) I just want to check
        that the Copy object was created with the correct name fields from the request
         */
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().getBookId(), is(bookId));
        verify(copyService).createCopy(any());
    }

}