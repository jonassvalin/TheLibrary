package library.service;

import library.domain.Copy;
import library.domain.Loan;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import static library.domain.Copy.copyBuilder;
import static library.domain.Loan.loanBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class LoanServiceTest {

    private LoanService loanService;

    @Mock
    private CopyService copyService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        loanService = new LoanService(copyService);
    }

    /*
    Some tests in this class are a little contrived as they're basically testing the same methods over and over.
    Normally you wouldn't have the storage actually in the service, but rather in a separate repository,
    in which case you would instead verify that the repository was correctly called, and then the individual
    tests would be more unique.
     */

    @Test
    public void shouldGetAllLoans() throws Exception {
        // given
        final String copyIdOne = "someCopyId";
        final String copyIdTwo = "someOtherCopyId";
        final Copy copy = copyBuilder()
                .withBookId("someBookId")
                .build();
        // We can return the same copy even if its semantically incorrect since all we want to check is existence
        when(copyService.getCopy(copyIdOne)).thenReturn(Optional.of(copy));
        when(copyService.getCopy(copyIdTwo)).thenReturn(Optional.of(copy));
        final Loan loanOne = loanService.createLoan(loanBuilder()
                .withCopyId(copyIdOne)
                .withEmployeeId("someEmployeeId")
                .withReturnDate(Instant.now().plusSeconds(10))
                .build())
                .get();
        final Loan loanTwo = loanService.createLoan(loanBuilder()
                .withCopyId(copyIdTwo)
                .withEmployeeId("someOtherEmployeeId")
                .withReturnDate(Instant.now().plusSeconds(10))
                .build())
                .get();

        // when
        final Collection<Loan> allCopies = loanService.getAllLoans(null);

        // then
        assertThat(allCopies, containsInAnyOrder(loanOne, loanTwo));
    }

    @Test
    public void shouldGetAllLoansForSpecificEmployee() throws Exception {
        // given
        final String copyIdOne = "someCopyId";
        final String copyIdTwo = "someOtherCopyId";
        final String employeeId = "someEmployeeId";
        final Copy copy = copyBuilder()
                .withBookId("someBookId")
                .build();
        // We can return the same copy even if its semantically incorrect since all we want to check is existence
        when(copyService.getCopy(copyIdOne)).thenReturn(Optional.of(copy));
        when(copyService.getCopy(copyIdTwo)).thenReturn(Optional.of(copy));
        final Loan loanWithMatchingEmployeeId = loanService.createLoan(loanBuilder()
                .withCopyId(copyIdOne)
                .withEmployeeId(employeeId)
                .withReturnDate(Instant.now().plusSeconds(10))
                .build())
                .get();
        loanService.createLoan(loanBuilder()
                .withCopyId(copyIdTwo)
                .withEmployeeId("someOtherEmployeeId")
                .withReturnDate(Instant.now().plusSeconds(10))
                .build());

        // when
        final Collection<Loan> allCopies = loanService.getAllLoans(employeeId);

        // then
        assertThat(allCopies, containsInAnyOrder(loanWithMatchingEmployeeId));
    }

    @Test
    public void shouldGetLoan() throws Exception {
        // given
        final String copyId = "someCopyId";
        final Copy copy = copyBuilder()
                .withBookId("someBookId")
                .build();
        when(copyService.getCopy(copyId)).thenReturn(Optional.of(copy));
        final Loan loan = loanService.createLoan(loanBuilder()
                .withCopyId(copyId)
                .withEmployeeId("someEmployeeId")
                .withReturnDate(Instant.now().plusSeconds(10))
                .build())
                .get();

        // when
        final Optional<Loan> storedLoan = loanService.getLoan(loan.getId());

        // then
        assertThat(storedLoan, is(Optional.of(loan)));
    }

    @Test
    public void shouldReturnEmptyIfLoneDoesNotExist() throws Exception {
        // when
        final Optional<Loan> storedLoan = loanService.getLoan("someId");

        // then
        assertThat(storedLoan, is(Optional.empty()));
    }

    @Test
    public void shouldCreateLoan() throws Exception {
        // given
        final String copyId = "someCopyId";
        final Copy copy = copyBuilder()
                .withBookId("someBookId")
                .build();
        final Loan loan = loanBuilder()
                .withCopyId(copyId)
                .withEmployeeId("someEmployeeId")
                .withReturnDate(Instant.now().plusSeconds(10))
                .build();
        when(copyService.getCopy(copyId)).thenReturn(Optional.of(copy));

        // when
        final Optional<Loan> returnedLoan = loanService.createLoan(loan);

        // then
        assertThat(returnedLoan, is(Optional.of(loan)));
        assertThat(loanService.getLoan(loan.getId()), is(Optional.of(loan)));
    }

    @Test
    public void shouldReturnExistingLoanIfOneAlreadyExists() throws Exception {
        // given
        final String copyId = "someCopyId";
        final Copy copy = copyBuilder()
                .withBookId("someBookId")
                .build();
        final Loan oldLoan = loanBuilder()
                .withCopyId(copyId)
                .withEmployeeId("someEmployeeId")
                .withReturnDate(Instant.now().plusSeconds(10))
                .build();
        when(copyService.getCopy(copyId)).thenReturn(Optional.of(copy));
        loanService.createLoan(oldLoan);

        // when
        final Loan newLoan = loanBuilder()
                .withCopyId(copyId)
                .withEmployeeId("someOtherEmployeeId")
                .withReturnDate(Instant.now().plusSeconds(10))
                .build();
        final Optional<Loan> returnedLoan = loanService.createLoan(newLoan);

        // then
        assertThat(returnedLoan, is(Optional.of(oldLoan)));
        assertThat(loanService.getLoan(oldLoan.getId()), is(Optional.of(oldLoan)));
    }

    @Test
    public void shouldDeleteLoan() throws Exception {
        // given
        final String copyId = "someCopyId";
        final Copy copy = copyBuilder()
                .withBookId("someBookId")
                .build();
        final Loan loan = loanBuilder()
                .withCopyId(copyId)
                .withEmployeeId("someEmployeeId")
                .withReturnDate(Instant.now().plusSeconds(10))
                .build();
        when(copyService.getCopy(copyId)).thenReturn(Optional.of(copy));
        loanService.createLoan(loan);

        // when
        final boolean deleted = loanService.deleteLoan(loan.getId());

        // then
        assertThat(deleted, is(true));
    }

    @Test
    public void shouldReturnFalseIfLoanNotFound() throws Exception {
        // when
        final boolean deleted = loanService.deleteLoan("someId");

        // then
        assertThat(deleted, is(false));
    }

}