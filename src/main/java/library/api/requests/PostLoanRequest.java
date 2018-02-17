package library.api.requests;

public class PostLoanRequest {

    private String copyId;

    private String employeeId;

    private String returnDate;

    // empty constructor needed for JSON marshalling magic
    public PostLoanRequest() {
    }

    // constructor only needed for tests, hence skipping the extra boiler plate of adding a builder
    public PostLoanRequest(final String copyId, final String employeeId, final String returnDate) {
        this.copyId = copyId;
        this.employeeId = employeeId;
        this.returnDate = returnDate;
    }

    public String getCopyId() {
        return copyId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getReturnDate() {
        return returnDate;
    }
}