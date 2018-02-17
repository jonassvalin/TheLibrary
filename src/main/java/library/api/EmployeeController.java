package library.api;

import library.api.requests.PostEmployeeRequest;
import library.domain.Employee;
import library.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

import static library.domain.Employee.employeeBuilder;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(final EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Employee>> getAllEmployees() {
        return new ResponseEntity<>(employeeService.getAllEmployees(), OK);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> getEmployee(@PathVariable final String id) {
        return employeeService
                .getEmployee(id)
                .map(employee -> new ResponseEntity<>(employee, OK))
                .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> createEmployee(@RequestBody final PostEmployeeRequest request) {
        return new ResponseEntity<>(
                employeeService
                        .createEmployee(employeeBuilder()
                                .withFirstName(request.getFirstName())
                                .withLastName(request.getLastName())
                                .build()),
                OK);
    }

}
