package library.api;

import library.api.requests.PostEmployeeRequest;
import library.domain.Employee;
import library.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static library.domain.Employee.employeeBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

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
                .withFirstName("someFirstName")
                .withLastName("someLastName")
                .build();

        when(employeeService.getEmployee(id)).thenReturn(Optional.of(employee));

        // when
        final ResponseEntity<Employee> response = employeeController.getEmployee(id);

        // then
        final ResponseEntity<Employee> expectedResponse = new ResponseEntity<>(employee, OK);
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

    @Test
    public void shouldGetAllEmployees() throws Exception {
        // given
        final Employee employee = employeeBuilder()
                .withFirstName("someFirstName")
                .withLastName("someLastName")
                .build();
        when(employeeService.getAllEmployees()).thenReturn(singletonList(employee));

        // when
        final ResponseEntity<Collection<Employee>> response = employeeController.getAllEmployees();

        // then
        final ResponseEntity<Collection<Employee>> expectedResponse = new ResponseEntity<>(singletonList(employee), OK);
        assertThat(response, is(expectedResponse));
        verify(employeeService.getAllEmployees());
    }

    @Test
    public void shouldCreateEmployee() throws Exception {
        // given
        final String firstName = "someFirstName";
        final String lastName = "someLastName";
        when(employeeService.createEmployee(any())).thenAnswer(methodCall -> methodCall.getArgumentAt(0, Employee.class));

        // when
        final ResponseEntity<Employee> response = employeeController.createEmployee(new PostEmployeeRequest(firstName, lastName));

        // then
        /*
        Normally you would probably have some sort of converter class to convert between the Request and the Employee,
        in which case you would verify that the call to the converter was correctly performed and that the Controller
        returned the value Employee object returned by the converter. In this case, since we're doing the conversion
        directly in the Controller (keeping within the time constraints of the assignment) I just want to check
        that the Employee object was created with the correct name fields from the request
         */
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().getFirstName(), is(firstName));
        assertThat(response.getBody().getLastName(), is(lastName));
    }

}