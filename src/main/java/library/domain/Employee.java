package library.domain;

public class Employee {

    private final String id;

    private Employee(final EmployeeBuilder builder) {
        this.id = builder.id;
    }

    public String getId() {
        return id;
    }

    public static EmployeeBuilder employeeBuilder() {
        return new EmployeeBuilder();
    }

    public static class EmployeeBuilder {

        private String id;

        private EmployeeBuilder() {
        }

        public EmployeeBuilder withId(final String id) {
            this.id = id;
            return this;
        }

        public Employee build() {
            return new Employee(this);
        }
    }
}
