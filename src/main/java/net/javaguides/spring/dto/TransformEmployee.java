package net.javaguides.spring.dto;

import org.springframework.stereotype.Component;

import net.javaguides.spring.model.Employee;

@Component
public class TransformEmployee implements TransformModelToDTO<Employee, EmployeeDTO> {

  @Override
  public EmployeeDTO transformFrom1stTo2nd(Employee employee) {
    if (employee == null) {
      return null;
    }

    return EmployeeDTO.builder()
        .id(employee.getId())
        .firstName(employee.getFirstName())
        .lastName(employee.getLastName())
        .email(employee.getEmail())
        .build();
  }

  @Override
  public Employee transformFrom2ndTo1st(EmployeeDTO employeeDTO) {
    if (employeeDTO == null) {
      return null;
    }

    return Employee.builder()
        .id(employeeDTO.getId())
        .firstName(employeeDTO.getFirstName())
        .lastName(employeeDTO.getLastName())
        .email(employeeDTO.getEmail())
        .build();
  }
}
