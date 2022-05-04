package net.javaguides.spring.repository;

import static org.assertj.core.api.Assertions.assertThat;

//import org.junit.jupiter.api.DisplayName;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import net.javaguides.spring.model.Employee;

@DataJpaTest
class EmployeeRepositoryTests {

  private static final String FIRST_NAME_FILTER = "Elkin";
  private static final String LAST_NAME_FILTER = "Torres";
  @Autowired
  private EmployeeRepository employeeRepository;

  private Employee employee;

  @BeforeEach
  void setup() {
    employee = Employee.builder()
        .firstName("Elkin")
        .lastName("Torres")
        .email("elkin@vita.com")
        .build();
  }

  //@DisplayName("JUnit test for save employee operation")
  @Test
  void givenNewEmployee_whenSave_thenSavedEmployee() {
    //given - precondition or setup
//    Employee employee = Employee.builder().firstName("Elkin").lastName("Torres").email("elkin@vita.com").build();

    // when - action or the behaviour that we are going to test
    Employee savedEmployee = employeeRepository.save(employee);

    // then - verify the output
    assertThat(savedEmployee).isNotNull();
    assertThat(savedEmployee.getId()).isPositive();
  }

  @Test
  void givenExistingEmployees_whenFindAll_thenFoundEmployees() {
    //given - precondition or setup
    employeeRepository.save(employee);

    Employee employee2 = Employee.builder().firstName("Valita").lastName("Torres").email("muñeca@hermosa.com").build();
    employeeRepository.save(employee2);
    // when - action or the behaviour that we are going to test
    List<Employee> employeeList = employeeRepository.findAll();

    // then - verify the output
    assertThat(employeeList)
        .isNotNull()
        .hasSize(2);
  }

  @Test
  void givenExistingEmployee_whenFindById_thenFoundEmployee() {
    //given - precondition or setup
    employeeRepository.save(employee);

    // when - action or the behaviour that we are going to test
    Employee dbEmployee = employeeRepository.findById(employee.getId()).get();

    // then - verify the output
    assertThat(dbEmployee).isNotNull();
    assertThat(dbEmployee.getEmail()).isEqualTo("elkin@vita.com");
  }

  @Test
  void givenExistingEmployee_whenFindByEmail_thenFoundEmployee() {
    //given - precondition or setup
    employeeRepository.save(employee);

    // when - action or the behaviour that we are going to test
    Employee dbEmployee = employeeRepository.findByEmail(employee.getEmail()).get();

    // then - verify the output
    assertThat(dbEmployee).isNotNull();
    assertThat(dbEmployee.getFirstName()).isEqualTo("Elkin");
  }

  @Test
  void givenExistingEmployee_whenUpdate_thenUpdatedEmployee() {
    //given - precondition or setup
    employeeRepository.save(employee);

    // when - action or the behaviour that we are going to test
    Employee dbEmployee = employeeRepository.findById(employee.getId()).get();
    dbEmployee.setFirstName("Sofi");
    dbEmployee.setLastName("Echeverria");
    dbEmployee.setEmail("sofi@hermosa.com");
    Employee updatedEmployee = employeeRepository.save(dbEmployee);

    // then - verify the output
    assertThat(updatedEmployee).isNotNull();
    assertThat(updatedEmployee.getFirstName()).isEqualTo("Sofi");
    assertThat(updatedEmployee.getLastName()).isEqualTo("Echeverria");
    assertThat(updatedEmployee.getEmail()).isEqualTo("sofi@hermosa.com");
  }

  @Test
  void givenExistingEmployee_whenDelete_thenEmployeeNotFound() {
    //given - precondition or setup
    employeeRepository.save(employee);

    // when - action or the behaviour that we are going to test
    employeeRepository.delete(employee);
    Optional<Employee> employeeOpt = employeeRepository.findById(employee.getId());

    // then - verify the output
    assertThat(employeeOpt).isEmpty();
  }

  @Test
  void givenExistingEmployeeAndFilters_whenFindByJPQL_thenFoundEmployee() {
    //given - precondition or setup
    employeeRepository.save(employee);

    // when - action or the behaviour that we are going to test
    Employee dbEmployee = employeeRepository.findByJPQL(FIRST_NAME_FILTER, LAST_NAME_FILTER);

    // then - verify the output
    assertThat(dbEmployee).isNotNull();
    assertThat(dbEmployee.getLastName()).isEqualTo("Torres");
  }

  @Test
  void givenExistingEmployeeAndFilters_whenFindByJPQLNamedParams_thenFoundEmployee() {
    //given - precondition or setup
    employeeRepository.save(employee);

    // when - action or the behaviour that we are going to test
    Employee dbEmployee = employeeRepository.findByJPQLNamedParams(FIRST_NAME_FILTER, LAST_NAME_FILTER);

    // then - verify the output
    assertThat(dbEmployee).isNotNull();
    assertThat(dbEmployee.getLastName()).isEqualTo("Torres");
  }

  @Test
  void givenExistingEmployeeAndFilters_whenFindByNativeSQL_thenFoundEmployee() {
    //given - precondition or setup
    employeeRepository.save(employee);

    // when - action or the behaviour that we are going to test
    Employee dbEmployee = employeeRepository.findByNativeSQL(FIRST_NAME_FILTER, LAST_NAME_FILTER);

    // then - verify the output
    assertThat(dbEmployee).isNotNull();
    assertThat(dbEmployee.getLastName()).isEqualTo("Torres");
  }

  @Test
  void givenExistingEmployeeAndFilters_whenFindByNativeSQLNamedParams_thenFoundEmployee() {
    //given - precondition or setup
    employeeRepository.save(employee);

    // when - action or the behaviour that we are going to test
    Employee dbEmployee = employeeRepository.findByNativeSQLNamedParams(FIRST_NAME_FILTER, LAST_NAME_FILTER);

    // then - verify the output
    assertThat(dbEmployee).isNotNull();
    assertThat(dbEmployee.getLastName()).isEqualTo("Torres");
  }

}
