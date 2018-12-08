package org.fkjava.hr.repository;

import java.util.List;

import org.fkjava.hr.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String>{

	Department findByParentAndName(Department parent, String name);

	Department findByNameAndParentNull(String name);

	@Query("select max(number) from Department where parent is null")
	Double findMaxNubmerByParentNull();

	@Query("select max(number) from Department where parent = :parent")
	Double findMaxNumberByParent(@Param("parent")Department parent);

	List<Department> findByParentNullOrderByNumber();

}
