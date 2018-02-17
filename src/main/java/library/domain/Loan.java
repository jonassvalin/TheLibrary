package library.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Loan {

    private final String id;

    private final String copyId;

    private final String employeeId;

    private final Instant returnDate;

    private final Instant createdAt;

    /*
    Here we should probably perform some validation such that returnDate can not be before
    the creation date, but we will skip that for now.
     */
    private Loan(final Loan.LoanBuilder builder) {
        Objects.requireNonNull(builder.copyId, "copyId must not be null");
        Objects.requireNonNull(builder.employeeId, "employeeId must not be null");
        Objects.requireNonNull(builder.returnDate, "returnDate must not be null");

        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.copyId = builder.copyId;
        this.employeeId = builder.employeeId;
        this.returnDate = builder.returnDate;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Loan)) return false;
        final Loan that = (Loan) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(copyId, that.copyId) &&
                Objects.equals(employeeId, that.employeeId) &&
                Objects.equals(returnDate, that.returnDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, copyId, employeeId, returnDate);
    }

    public String getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getCopyId() {
        return copyId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public Instant getReturnDate() {
        return returnDate;
    }

    public static Loan.LoanBuilder loanBuilder() {
        return new Loan.LoanBuilder();
    }

    public static class LoanBuilder {

        private String copyId;

        private String employeeId;

        private Instant returnDate;

        private LoanBuilder() {
        }

        public Loan.LoanBuilder withCopyId(final String copyId) {
            this.copyId = copyId;
            return this;
        }

        public Loan.LoanBuilder withEmployeeId(final String employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public Loan.LoanBuilder withReturnDate(final Instant returnDate) {
            this.returnDate = returnDate;
            return this;
        }

        public Loan build() {
            return new Loan(this);
        }
    }
}
