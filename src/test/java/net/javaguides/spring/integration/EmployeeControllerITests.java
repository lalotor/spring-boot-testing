package net.javaguides.spring.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.javaguides.spring.model.Employee;
import net.javaguides.spring.repository.EmployeeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITests {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private EmployeeRepository employeeRepository;

  private Employee employee;

  @BeforeEach
  public void setup() {
    employee = Employee.builder().id(1L).firstName("Elkin").lastName("Torres").email("elkin@vita.com").build();

    employeeRepository.deleteAll();
  }

  @Test
  void givenNewEmployee_whenCreateEmployee_thenCreatedEmployee() throws Exception {
    //given - precondition or setup

    // when - action or the behaviour that we are going to test
    ResultActions response = mockMvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(employee)));

    // then - verify the output
    response.andDo(print()).andExpect(status().isCreated())
        .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
        .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
        .andExpect(jsonPath("$.email", is(employee.getEmail())));
  }

  @Test
  void givenExistingEmployees_whenGetAllEmployees_thenFoundEmployees() throws Exception {
    //given - precondition or setup
    Employee employee2 = Employee.builder().id(2L).firstName("Sofi").lastName("Torres").email("sofi@vita.com").build();
    var employeesList = List.of(employee, employee2);
    employeeRepository.saveAll(employeesList);

    // when - action or the behaviour that we are going to test
    ResultActions response = mockMvc.perform(get("/api/employees"));

    // then - verify the output
    response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(employeesList.size())));
  }

  @Test
  void givenExistingEmployee_whenGetEmployeeById_thenFoundEmployee() throws Exception {
    //given - precondition or setup
    employee = employeeRepository.save(employee);

    // when - action or the behaviour that we are going to test
    ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

    // then - verify the output
    response.andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
        .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
        .andExpect(jsonPath("$.email", is(employee.getEmail())));
  }

  @Test
  void givenNonExistingEmployee_whenGetEmployeeById_thenEmpty() throws Exception {
    //given - precondition or setup
    long employeeId = 1L;

    // when - action or the behaviour that we are going to test
    ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

    // then - verify the output
    response.andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void givenExistingEmployee_whenUpdateEmployee_thenUpdatedEmployee() throws Exception {
    //given - precondition or setup
    employee = employeeRepository.save(employee);
    employee.setEmail("test@test.org");
    employee.setFirstName("Test");

    // when - action or the behaviour that we are going to test
    ResultActions response = mockMvc.perform(put("/api/employees")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(employee)));

    // then - verify the output
    response.andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName", is("Test")))
        .andExpect(jsonPath("$.email", is("test@test.org")));
  }

  @Test
  void givenNonExistingEmployee_whenUpdateEmployee_thenUpdatedEmpty() throws Exception {
    //given - precondition or setup
    employee = employeeRepository.save(employee);
    employee.setId(0L);

    // when - action or the behaviour that we are going to test
    ResultActions response = mockMvc.perform(put("/api/employees").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(employee)));

    // then - verify the output
    response.andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void givenExistingEmployee_whenDeleteEmployee_thenOK() throws Exception {
    //given - precondition or setup
    employee = employeeRepository.save(employee);

    // when - action or the behaviour that we are going to test
    ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employee.getId()));

    // then - verify the output
    response.andDo(print())
        .andExpect(status().isOk());
  }

}
