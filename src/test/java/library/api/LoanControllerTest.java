package library.api;

import library.api.requests.PostLoanRequest;
import library.domain.Loan;
import library.service.LoanService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static library.domain.Loan.loanBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

public class LoanControllerTest {

    private LoanController loanController;

    @Mock
    private LoanService loanService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        loanController = new LoanController(loanService);
    }

    @Test
    public void shouldCreateLoan() throws Exception {
        // given
        final String copyId = "someCopyId";
        final String employeeId = "employeeId";
        final String returnDateString = "2018-02-03";
        final PostLoanRequest postLoanRequest = new PostLoanRequest(copyId, employeeId, returnDateString);
        when(loanService.createLoan(any())).thenAnswer(methodCall -> Optional.of(methodCall.getArgumentAt(0, Loan.class)));

        // when
        final ResponseEntity<Loan> response = loanController.createLoan(postLoanRequest);

        // then
        /*
        Normally you would probably have some sort of converter class to convert between the Request and the Loan,
        in which case you would verify that the call to the converter was correctly performed and that the Controller
        returned the value Loan object returned by the converter. In this case, since we're doing the conversion
        directly in the Controller (keeping within the time constraints of the assignment) I just want to check
        that the Loan object was created with the correct fields from the request
         */
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().getCopyId(), is(copyId));
        assertThat(response.getBody().getEmployeeId(), is(employeeId));
        assertThat(response.getBody().getReturnDate(), is(LocalDate.parse(returnDateString).atStartOfDay(ZoneId.of("Europe/London")).toInstant()));
        verify(loanService).createLoan(any());
    }

    @Test
    public void shouldGetLoan() throws Exception {
        // given
        final Loan loan = loanBuilder()
                .withCopyId("someCopyId")
                .withEmployeeId("someEmployeeId")
                .withReturnDate(Instant.now().plusSeconds(10))
                .build();
        when(loanService.getLoan(loan.getId())).thenReturn(Optional.of(loan));

        // when
        final ResponseEntity<Loan> response = loanController.getLoan(loan.getId());

        // then
        assertThat(response, is(new ResponseEntity<>(loan, OK)));
    }

    @Test
    public void shouldGetAllLoans() throws Exception {
        // given
        final String employeeId = "someEmployeeId";
        final Loan loan = loanBuilder()
                .withCopyId("someCopyId")
                .withEmployeeId("someEmployeeId")
                .withReturnDate(Instant.now().plusSeconds(10))
                .build();
        when(loanService.getAllLoans(employeeId)).thenReturn(singletonList(loan));

        // when
        final ResponseEntity<Collection<Loan>> response = loanController.getAllLoans(employeeId);

        // then
        assertThat(response, is(new ResponseEntity<>(singletonList(loan), OK)));
    }

}