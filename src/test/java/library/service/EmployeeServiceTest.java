package library.service;

import library.domain.Employee;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Optional;

import static library.domain.Employee.employeeBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class EmployeeServiceTest {

    private EmployeeService employeeService;

    @Before
    public void setUp() throws Exception {
        employeeService = new EmployeeService();
    }

    /*
    The tests in this class are a little contrived as they're basically testing the same methods over and over.
    Normally you wouldn't have the storage actually in the service, but rather in a separate repository,
    in which case you would instead verify that the repository was correctly called, and then the individual
    tests would be more unique.
     */

    @Test
    public void shouldGetAllEmployees() throws Exception {
        // given
        final Employee someEmployee = employeeService.createEmployee(employeeBuilder()
                .withFirstName("someFirstName")
                .withLastName("someLastName")
                .build());
        final Employee someOtherEmployee = employeeService.createEmployee(employeeBuilder()
                .withFirstName("someOtherFirstName")
                .withLastName("someOtherLastName")
                .build());

        // when
        final Collection<Employee> allEmployees = employeeService.getAllEmployees();

        // then
        assertThat(allEmployees, containsInAnyOrder(someEmployee, someOtherEmployee));
    }

    @Test
    public void shouldCreateEmployee() throws Exception {
        // given
        final Employee employee = employeeBuilder()
                .withFirstName("someFirstName")
                .withLastName("someLastName")
                .build();

        // when
        employeeService.createEmployee(employee);

        // then
        final Optional<Employee> storedEmployee = employeeService.getEmployee(employee.getId());
        assertThat(storedEmployee, is(Optional.of(employee)));
    }

    @Test
    public void shouldGetEmployee() throws Exception {
        // given
        final Employee employee = employeeService.createEmployee(employeeBuilder()
                .withFirstName("someFirstName")
                .withLastName("someLastName")
                .build());

        // when
        final Optional<Employee> storedEmployee = employeeService.getEmployee(employee.getId());

        // then
        assertThat(storedEmployee, is(Optional.of(employee)));
    }

}