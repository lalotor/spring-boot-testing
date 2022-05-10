package net.javaguides.spring.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.javaguides.spring.exception.ResourceNotFoundException;
import net.javaguides.spring.model.Employee;
import net.javaguides.spring.repository.EmployeeRepository;
import net.javaguides.spring.service.EmployeeService;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;

  @Override
  public Employee saveEmployee(Employee employee) {
    Optional<Employee> employeeOpt = employeeRepository.findByEmail(employee.getEmail());
    if (employeeOpt.isPresent()) {
      throw new ResourceNotFoundException("Employee already exist with given email: " + employee.getEmail());
    }

    return employeeRepository.save(employee);
  }

  @Override
  public List<Employee> getAllEmployees() {
    return employeeRepository.findAll();
  }

  @Override
  public Optional<Employee> getEmployeeById(long id) {
    return employeeRepository.findById(id);
  }

  @Override
  public Employee updateEmployee(Employee employee) {
    return employeeRepository.save(employee);
  }

  @Override
  public void deleteEmployee(long id) {
    employeeRepository.deleteById(id);
  }
}
