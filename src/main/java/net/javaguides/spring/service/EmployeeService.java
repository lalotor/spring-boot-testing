package net.javaguides.spring.service;

import java.util.List;
import java.util.Optional;

import net.javaguides.spring.model.Employee;

public interface EmployeeService {
  Employee saveEmployee(Employee employee);
  List<Employee> getAllEmployees();
  Optional<Employee> getEmployeeById(long id);
  Employee updateEmployee(Employee employee);
  void deleteEmployee(long id);
}
