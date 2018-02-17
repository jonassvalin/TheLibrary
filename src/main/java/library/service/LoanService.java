package library.service;

import library.domain.Loan;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/*
The loan could have been designed as a sub-resource nested inside the Copy object. The reason
I did not go with that solution is that the implementation of having a separate LoanService
that is responsible for all loan objects means that it would be easier to keep a history of
loans (even though that isn't done in the current solution).
 */
@Component
public class LoanService {

    private final HashMap<String, Loan> loans;

    private final CopyService copyService;

    public LoanService(final CopyService copyService) {
        this.copyService = copyService;
        loans = new HashMap<>();
    }

    public Collection<Loan> getAllLoans(final String employeeId) {
        if (Objects.isNull(employeeId)) {
            return loans.values();
        }
        return loans.values()
                .stream()
                .filter(loan -> loan.getEmployeeId().equals(employeeId))
                .collect(Collectors.toList());
    }

    // Prefer to work with Optionals to avoid needing null checks
    public Optional<Loan> getLoan(final String id) {
        return Optional.ofNullable(loans.get(id));
    }

    /*
    This method became a bit messy due to the three scenarios of the 1) successful creation, 2) referenced
    Copy doesn't exist, 3) a Loan for the Copy already exist. With more time I would've refactored it along
    with the calling Controller method. Missing is also the check for existence of Employee, but you get
    the idea...
    */
    public Optional<Loan> createLoan(final Loan loan) {
        if (!copyService.getCopy(loan.getCopyId()).isPresent()) {
            return Optional.empty();
        }

        final Optional<Loan> currentLoan = loans.values()
                .stream()
                .filter(existingLoan -> existingLoan.getCopyId().equals(loan.getCopyId()))
                .findAny();

        if (currentLoan.isPresent()) {
            return currentLoan;
        }

        loans.put(loan.getId(), loan);
        return Optional.of(loans.get(loan.getId()));
    }

    public boolean deleteLoan(final String id) {
        return Objects.nonNull(loans.remove(id));
    }
}
