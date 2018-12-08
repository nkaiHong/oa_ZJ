package org.fkjava.identity.service.impl;

import java.util.List;

import org.fkjava.identity.domain.Role;
import org.fkjava.identity.repository.RoleRepository;
import org.fkjava.identity.service.RoleService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class RoleServiceImpl implements RoleService,InitializingBean{

	@Autowired
	private RoleRepository roleRepository;
	
	//初始化，只会执行一次
	@Override	
	@Transactional
	public void afterPropertiesSet() throws Exception {
		//在服务器启动的时候，检查是否有预置的角色，如果没有则自动加上
		//需要手动加入给用户，比较牛逼
		//如果调用持久层先查询用户的角色，如果在数据库里面找到，则使用找到的，否则，就查询new一个新的
		Role admin = this.roleRepository.findByRoleKey("ADMIN").orElse(new Role());
		//超级管理员的设置
		admin.setName("超级管理员");
		admin.setRoleKey("ADMIN");
		//调用下面的save方法保存
		this.roleRepository.save(admin);
		
		//所有用户的默认角色   --->每一个用户都会有普通用户的身份
		//如果调用持久层先查询用户的角色，如果在数据库里面找到，则使用找到的，否则，就查询new一个新的
		Role user = this.roleRepository.findByRoleKey("USER").orElse(new Role());
		//普通用户的设置
		user.setName("普通用户");
		user.setRoleKey("USER");
		//调用下面的save方法保存
		this.roleRepository.save(admin);
		
	}
	
	@Override	
	public List<Role> findAllRoles() {
		
		return this.roleRepository.findAll();
	}
	
	@Override
	public void save(Role role) {
		//在保存的时候，如果角色的id等于空，那么就直接设置为空
		if(StringUtils.isEmpty(role.getId())) {
			role.setId(null);
		}
		//在保存的时候业务逻辑层需要到持久层里面查询数据，看是否有对应的key,因为key是唯一的，再作判断
		//如果数据库里面有key表示修改，如果没有，表示新增
		//第二天再此处修，在持久层接口里面改为optional，函数式接口，这里要加上一个判断。方便后面预知角色
		
		Role old = roleRepository.findByRoleKey(role.getRoleKey()).orElse(null);
		if(		//如果页面传过来的id为空，表示新增，并且数据库里面没有找到key值，表示没有重复的角色，新增
				(role.getId() == null && old == null)
				// 修改的时候，根据key从数据库找到一个Role，但是id相同，此时只是修改了名称、没有修改key
				/*如果页面传过来的id不为空，并且在数据库也找到一个角色，并且页面的传过来的id和数据库的id相同
				 * 此时只是修改了名称、没有修改key*/
			|| (role.getId() != null && old != null && role.getId().equals(old.getId()))
			// 修改，但是key从数据库里面没有找到记录，表示key也修改了！
			|| (role.getId() != null && old == null)){
			
			roleRepository.save(role);
		}else {
			// 要么id为空、数据库不为空
			// 要么id不为空、数据库也不为空，但是id不相同
			throw new IllegalArgumentException("角色的KEY是唯一的，不能重复");
		}
	}
	
	//调用持久层roleRepository删除id,因为是使用Spring-boot,所以实现不需要自己写，由Spring-boot封装好，直接用
	@Override
	public void deleteById(String id) {
		roleRepository.deleteById(id);	
	}

	@Override
	public List<Role> findAllNotFixed() {
		
		return this.roleRepository.findByFixedFalseOrderByName();
	}

	@Override
	public List<Role> findAll() {
		
		return this.roleRepository.findAll();
	}

	

}
