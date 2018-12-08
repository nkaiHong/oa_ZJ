package org.fkjava.identity.repository;



import java.util.List;
import java.util.Optional;

import org.fkjava.identity.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String>{

	Optional<Role> findByRoleKey(String roleKey);

	List<Role> findByFixedTrue();

	//查询之后根据名字排序，跟前面的查询分隔开
	List<Role> findByFixedFalseOrderByName();

	

	
}
