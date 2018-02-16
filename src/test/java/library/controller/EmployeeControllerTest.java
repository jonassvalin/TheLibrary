package library.controller;

import library.domain.Employee;
import library.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static library.domain.Employee.employeeBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class EmployeeControllerTest {

    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        employeeController = new EmployeeController(employeeService);
    }

    @Test
    public void shouldGetEmployee() throws Exception {
        // given
        final String id = "someId";
        final Employee employee = employeeBuilder()
                .withId(id)
                .build();

        when(employeeService.getEmployee(id)).thenReturn(Optional.of(employee));

        // when
        final ResponseEntity<Employee> response = employeeController.getEmployee(id);

        // then
        final ResponseEntity<Employee> expectedResponse = new ResponseEntity<>(employee, HttpStatus.OK);
        assertThat(response, is(expectedResponse));
    }

    @Test
    public void shouldReturnNotFound() throws Exception {
        // given
        final String id = "someId";
        when(employeeService.getEmployee(id)).thenReturn(Optional.empty());

        // when
        final ResponseEntity<Employee> response = employeeController.getEmployee(id);

        // then
        final ResponseEntity<Employee> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        assertThat(response, is(expectedResponse));
    }

}