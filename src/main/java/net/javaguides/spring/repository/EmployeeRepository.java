package net.javaguides.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.javaguides.spring.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  Optional<Employee> findByEmail(String email);

  @Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
  Employee findByJPQL(String firstName, String lastName);

  @Query("select e from Employee e where e.firstName = :first_name and e.lastName = :last_name")
  Employee findByJPQLNamedParams(@Param("first_name") String firstName, @Param("last_name") String lastName);

  @Query(value = "select * from employees e where e.first_name = ?1 and e.last_name = ?2", nativeQuery = true)
  Employee findByNativeSQL(String firstName, String lastName);

  @Query(value = "select * from employees e where e.first_name = :first_name and e.last_name = :last_name", nativeQuery = true)
  Employee findByNativeSQLNamedParams(@Param("first_name") String firstName, @Param("last_name") String lastName);

}
