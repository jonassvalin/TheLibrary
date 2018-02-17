package library.api;

import library.api.requests.PostLoanRequest;
import library.domain.Loan;
import library.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;

import static library.domain.Loan.loanBuilder;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequestMapping("/loan")
public class LoanController {

    private final LoanService loanService;

    public LoanController(final LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Loan> createLoan(@RequestBody final PostLoanRequest request) {
        final Loan newLoan = loanBuilder()
                .withCopyId(request.getCopyId())
                .withEmployeeId(request.getEmployeeId())
                .withReturnDate(LocalDate.parse(request.getReturnDate()).atStartOfDay(ZoneId.of("Europe/London")).toInstant())
                .build();

        return loanService
                .createLoan(newLoan)
                .map(loan -> new ResponseEntity<>(loan, loan.equals(newLoan) ? OK : CONFLICT))
                .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Loan>> getAllLoans(@RequestParam(required = false) final String employeeId) {
        return new ResponseEntity<>(loanService.getAllLoans(employeeId), OK);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Loan> getLoan(@PathVariable final String id) {
        return loanService
                .getLoan(id)
                .map(loan -> new ResponseEntity<>(loan, OK))
                .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    }

    @DeleteMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Loan> deleteLoan(@PathVariable final String id) {
        return new ResponseEntity<>(loanService.deleteLoan(id) ? OK : NOT_FOUND);
    }
}
