package library.controller;

import library.domain.Employee;
import library.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(final EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable final String id) {
        return employeeService
                .getEmployee(id)
                .map(employee -> new ResponseEntity<>(employee, OK))
                .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    }

}
