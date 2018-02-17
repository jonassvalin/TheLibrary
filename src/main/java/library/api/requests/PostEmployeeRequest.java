package library.api.requests;

public class PostEmployeeRequest {

    private String firstName;

    private String lastName;

    // empty constructor needed for JSON marshalling magic
    public PostEmployeeRequest() {
    }

    // constructor only needed for tests, hence skipping the extra boiler plate of adding a builder
    public PostEmployeeRequest(final String firstName, final String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
