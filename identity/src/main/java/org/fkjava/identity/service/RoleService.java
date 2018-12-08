package org.fkjava.identity.service;

import java.util.List;

import org.fkjava.identity.domain.Role;

public interface RoleService {

	List<Role> findAllRoles();

	void save(Role role);

	void deleteById(String id);

	List<Role> findAllNotFixed();

	List<Role> findAll();

	
}
