package net.javaguides.spring.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.javaguides.spring.model.Employee;
import net.javaguides.spring.service.EmployeeService;

@WebMvcTest
class EmployeeControllerTests {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @MockBean
  private EmployeeService employeeService;

  private Employee employee;

  @BeforeEach
  public void setup() {
    employee = Employee.builder().id(1L).firstName("Elkin").lastName("Torres").email("elkin@vita.com").build();
  }

  @Test
  void givenNewEmployee_whenCreateEmployee_thenCreatedEmployee() throws Exception {
    //given - precondition or setup
    given(employeeService.saveEmployee(any(Employee.class))).willAnswer((invocation) -> invocation.getArgument(0));

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
    given(employeeService.getAllEmployees()).willReturn(List.of(employee, employee2));

    // when - action or the behaviour that we are going to test
    ResultActions response = mockMvc.perform(get("/api/employees"));

    // then - verify the output
    response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(2)));
  }

  @Test
  void givenExistingEmployee_whenGetEmployeeById_thenFoundEmployee() throws Exception {
    //given - precondition or setup
    long employeeId = 1L;
    given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

    // when - action or the behaviour that we are going to test
    ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

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
    given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

    // when - action or the behaviour that we are going to test
    ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

    // then - verify the output
    response.andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  void givenExistingEmployee_whenUpdateEmployee_thenUpdatedEmployee() throws Exception {
    //given - precondition or setup
    given(employeeService.getEmployeeById(employee.getId())).willReturn(Optional.of(employee));
    given(employeeService.updateEmployee(any(Employee.class))).willAnswer((invocation) -> invocation.getArgument(0));
    employee.setEmail("test@test.org");
    employee.setFirstName("Test");

    // when - action or the behaviour that we are going to test
    ResultActions response = mockMvc.perform(put("/api/employees").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(employee)));

    // then - verify the output
    response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.firstName", is("Test")))
        .andExpect(jsonPath("$.email", is("test@test.org")));
  }

  @Test
  void givenNonExistingEmployee_whenUpdateEmployee_thenUpdatedEmpty() throws Exception {
    //given - precondition or setup
    given(employeeService.getEmployeeById(employee.getId())).willReturn(Optional.empty());

    // when - action or the behaviour that we are going to test
    ResultActions response = mockMvc.perform(put("/api/employees").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(employee)));

    // then - verify the output
    response.andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  void givenExistingEmployee_whenDeleteEmployee_thenOK() throws Exception {
    //given - precondition or setup
    long employeeId = 1L;
    willDoNothing().given(employeeService).deleteEmployee(employeeId);

    // when - action or the behaviour that we are going to test
    ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

    // then - verify the output
    response.andDo(print()).andExpect(status().isOk());
  }

}