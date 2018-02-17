package library.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Employee {

    private final String id;

    private final String firstName;

    private final String lastName;

    private final Instant createdAt;

    private Employee(final EmployeeBuilder builder) {
        Objects.requireNonNull(builder.firstName, "firstName must not be null");
        Objects.requireNonNull(builder.lastName, "lastName must not be null");

        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        final Employee that = (Employee) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, firstName, lastName);
    }

    public String getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public static EmployeeBuilder employeeBuilder() {
        return new EmployeeBuilder();
    }

    public static class EmployeeBuilder {

        private String firstName;

        private String lastName;

        private EmployeeBuilder() {
        }

        public EmployeeBuilder withFirstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public EmployeeBuilder withLastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Employee build() {
            return new Employee(this);
        }
    }
}
