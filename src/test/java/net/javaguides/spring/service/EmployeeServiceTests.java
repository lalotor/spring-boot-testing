package net.javaguides.spring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.javaguides.spring.exception.ResourceNotFoundException;
import net.javaguides.spring.model.Employee;
import net.javaguides.spring.repository.EmployeeRepository;
import net.javaguides.spring.service.impl.EmployeeServiceImpl;

@ExtendWith({MockitoExtension.class})
class EmployeeServiceTests {

  @Mock
  private EmployeeRepository employeeRepository;
  @InjectMocks
  private EmployeeServiceImpl employeeService;
  private Employee employee;

  @BeforeEach
  public void setup() {
    employee = Employee.builder()
        .id(1L)
        .firstName("Elkin")
        .lastName("Torres")
        .email("elkin@vita.com")
        .build();
  }

  @Test
  void givenNewEmployee_whenSaveEmployee_thenSavedEmployee() {
    //given - precondition or setup
    given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
    given(employeeRepository.save(employee)).willReturn(employee);

    // when - action or the behaviour that we are going to test
    Employee dbEmployee = employeeService.saveEmployee(employee);

    // then - verify the output
    assertThat(dbEmployee).isNotNull();
  }

  @Test
  void givenExistingEmployee_whenSave_thenException() {
    //given - precondition or setup
    given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

    // when - action or the behaviour that we are going to test
    assertThrows(ResourceNotFoundException.class, () -> employeeService.saveEmployee(employee));

    // then - verify the output
    verify(employeeRepository, never()).save(any(Employee.class));
  }

  @Test
  void givenExistingEmployees_whenGetAllEmployees_thenFoundEmployees() {
    //given - precondition or setup
    Employee employee2 = Employee.builder()
        .id(2L)
        .firstName("Sofi")
        .lastName("Torres")
        .email("sofi@vita.com")
        .build();
    given(employeeRepository.findAll()).willReturn(List.of(employee, employee2));

    // when - action or the behaviour that we are going to test
    List<Employee> employeeList = employeeService.getAllEmployees();

    // then - verify the output
    assertThat(employeeList)
        .isNotNull()
        .hasSize(2);
  }

  @Test
  void givenEmptyEmployeesList_whenGetAllEmployees_thenNotFoundEmployees() {
    //given - precondition or setup
    given(employeeRepository.findAll()).willReturn(Collections.emptyList());

    // when - action or the behaviour that we are going to test
    List<Employee> employeeList = employeeService.getAllEmployees();

    // then - verify the output
    assertThat(employeeList).isEmpty();
  }

  @Test
  void givenExistingEmployee_whenGetEmployeeById_thenFoundEmployee() {
    //given - precondition or setup
    given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

    // when - action or the behaviour that we are going to test
    Employee dbEmployee = employeeService.getEmployeeById(employee.getId()).get();

    // then - verify the output
    assertThat(dbEmployee).isNotNull();
  }

  @Test
  void givenExistingEmployee_whenUpdateEmployee_thenUpdatedEmployee() {
    //given - precondition or setup
    given(employeeRepository.save(employee)).willReturn(employee);
    employee.setEmail("test@test.org");
    employee.setFirstName("Test");

    // when - action or the behaviour that we are going to test
    Employee dbEmployee = employeeService.updateEmployee(employee);

    // then - verify the output
    assertThat(dbEmployee).isNotNull();
    assertThat(dbEmployee.getEmail()).isEqualTo("test@test.org");
    assertThat(dbEmployee.getFirstName()).isEqualTo("Test");
  }

  @Test
  void givenExistingEmployee_whenDeleteEmployee_thenNothing() {
    //given - precondition or setup
    long employeeId = 1L;
    willDoNothing().given(employeeRepository).deleteById(employeeId);

    // when - action or the behaviour that we are going to test
    employeeService.deleteEmployee(employeeId);

    // then - verify the output
    verify(employeeRepository, times(1)).deleteById(employeeId);
  }

}