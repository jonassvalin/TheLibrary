package library.service;

import library.domain.Employee;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Component
public class EmployeeService {

    /**
     * Key: Id of the {@link library.domain.Employee}
     */
    private final HashMap<String, Employee> employees;

    public EmployeeService() {
        employees = new HashMap<>();
    }

    public Collection<Employee> getAllEmployees() {
        return employees.values();
    }

    // Prefer to work with Optionals to avoid null checks everywhere
    public Optional<Employee> getEmployee(final String id) {
        return Optional.ofNullable(employees.get(id));
    }

    /*
    This implementation is a bit odd, but I wanted to mimic the behaviour of a database call
    that would return the newly created object rather than a boolean or such,
    but HashMap.put returns the old value (or null), not the newly created value, hence the get call
    */
    public Employee createEmployee(final Employee employee) {
        employees.put(employee.getId(), employee);
        return employees.get(employee.getId());
    }
}
