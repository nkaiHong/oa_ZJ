package org.fkjava.hr.repository;

import org.fkjava.hr.domain.Employee;
import org.fkjava.identity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String>{

	Employee findByUser(User user);

}
