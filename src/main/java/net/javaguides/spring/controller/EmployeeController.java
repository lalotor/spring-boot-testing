package net.javaguides.spring.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.javaguides.spring.dto.EmployeeDTO;
import net.javaguides.spring.dto.TransformEmployee;
import net.javaguides.spring.model.Employee;
import net.javaguides.spring.service.EmployeeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {

  private final EmployeeService employeeService;
  private final TransformEmployee transformEmployee;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Employee createEmployee(@RequestBody EmployeeDTO employeeDTO) {
    return employeeService.saveEmployee(transformEmployee.transformFrom2ndTo1st(employeeDTO));
  }

  @GetMapping
  public List<Employee> getAllEmployess() {
    return employeeService.getAllEmployees();
  }

  @GetMapping("{id}")
  public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long employeeId) {
    return employeeService.getEmployeeById(employeeId)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping
  public ResponseEntity<Employee> updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
    Employee employee = transformEmployee.transformFrom2ndTo1st(employeeDTO);

    return employeeService.getEmployeeById(employee.getId())
        .map(dbEmployee -> employeeService.updateEmployee(employee))
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("{id}")
  public ResponseEntity<String> deleteEmployee(@PathVariable("id") long employeeId) {
    employeeService.deleteEmployee(employeeId);

    return new ResponseEntity<>("Employee deleted successfully", HttpStatus.OK);
  }

}
