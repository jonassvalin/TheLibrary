## The Library
This is a backend API for running a library within a company. The API allows you to define books, and create copies of
these books that employees can loan. I run it straight from Intellij using the LibraryApplication.class as main class.

## Usage
See controllers for full API options, but here are the essentials:

Create Employee: POST localhost:8080/employee
```
{
	"firstName": "Jonas",
	"lastName": "Svalin"
}
```

Create Book: POST localhost:8080/book
```
{
	"title": "The Pragmatic Programmer",
	"isbn": "123"
}
```

Create Copy of Book: POST localhost:8080/book/{bookId}/copy

Create Loan of Copy: POST localhost:8080/loan
```
{
	"copyId": "{copyId}",
	"employeeId": "{employeeId}",
	"returnDate": "YYYY-MM-DD"
}
```

Delete Loan of Copy: DELETE localhost:8080/loan/{id}