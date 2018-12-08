package org.fkjava.identity.repository;

import org.fkjava.identity.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, String>{

	User findByLoginName(String loginName);

	//相当于 where name = ?1;
	Page<User> findByName(String keyword, Pageable pageable);
	
	//或自动把查询条件前后使用 %包起来，使用like查询
	//相当于where name like ?1;
	Page<User> findByNameContaining(String keyword,Pageable pageable);

}
