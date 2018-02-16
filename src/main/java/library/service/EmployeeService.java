package library.service;

import library.domain.Employee;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static library.domain.Employee.employeeBuilder;

@Component
public class EmployeeService {

    public Optional<Employee> getEmployee(final String id) {
        return Optional.of(employeeBuilder()
            .withId(id)
            .build());
    }
}
