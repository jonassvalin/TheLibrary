package library.api.requests;

public class PostBookRequest {
    
    private String title;

    private String isbn;

    // empty constructor needed for JSON marshalling magic
    public PostBookRequest() {
    }

    // constructor only needed for tests, hence skipping the extra boiler plate of adding a builder
    public PostBookRequest(final String title, final String isbn) {
        this.title = title;
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }
}
