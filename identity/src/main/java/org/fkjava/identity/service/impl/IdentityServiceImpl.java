package org.fkjava.identity.service.impl;


import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


import org.fkjava.identity.domain.Role;
import org.fkjava.identity.domain.User;
import org.fkjava.identity.domain.User.Status;
import org.fkjava.identity.repository.RoleRepository;
import org.fkjava.identity.repository.UserDao;
import org.fkjava.identity.service.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class IdentityServiceImpl implements IdentityService{
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	@Transactional
	public void save(User user) {
		//处理角色：固定的角色、角色不能重复
		//查询所有的固定的角色   没有固定的定义为false
		//这里是查询数据里面所有的固定的角色，因为在domian层的设置为false，所以数据库里面的fiexd都是0
		//如果这里想查到数据，必须要手动改为1，也就是设置为true，才可以在数据库里面查到数据
		//否则永远为空，查不了数据，并且中间表的插入也做不了。
		List<Role> fixedRoles = roleRepository.findByFixedTrue();
		
		//2.由页面那边传入的角色
		List<Role> roles = user.getRoles();
		if(roles == null){
			// 不要相信页面的数据！
			// 页面可能传了一个空的集合进来，所以需要在这里判断是否为空、如果为空则自己创建一个
			roles = new LinkedList<>();
			//把前面设置好的roles 设置到user里面
			user.setRoles(roles);
		}else {
			//解决在JS添加角色的时候hashcCode重复的问题，因为hashCode重复，会导致后面的插入失败，只会插入第一个
			// 根据页面传递过来的id，查询所有的角色
			List<String> ids = new LinkedList<>();
			//转换为流式api
			roles.stream().map((role) ->{
				// role和id的映射关系
				// 我们希望：把Role里面的id全部获取出来
				return role.getId();
			}).forEach(id ->{
				// 迭代每个id，把id添加到集合里面
				ids.add(id);
			});
			System.out.println("1---->" +ids);
			
			//roles.stream().map(role -> role.getId()).forEach(ids::add);
			List<Role> tmp = this.roleRepository.findAllById(ids);
			System.out.println("2---->" +tmp);
			roles.clear();
			// 把页面传过来的角色，全部从数据库里面查询一遍！
			roles.addAll(tmp);
		}
		
		//3.确保角色不能重复，要重写Role的equals方法和hashCode方法，把所以的角色添加到Set集合里面自然就不重复！
		Set<Role> allRoles = new HashSet<>();
		allRoles.addAll(fixedRoles); //查询到的所有的固定角色
		allRoles.addAll(roles);		//页面那边传过来的角色，包括不固定的    如果后面加入的角色，已经存在Set里面，不会加入！
		
		//把整理后的角色放在user里面
		roles.clear();
		roles.addAll(allRoles);
		
		//1.检查是否没有id，如果没有id则把id设置为null，方便新增
		if(StringUtils.isEmpty(user.getId())) {
			user.setId(null);
		}
		
		//每次修改之后，都使用正常的状态 并且加上2个月的时间
		user.setStatus(Status.NORMAL);
		user.setExpiredTime(getExpiredTime());
		//2.根据登录名查询user对象，用于判断登录名是否被占用
		User old = userDao.findByLoginName(user.getLoginName());
		if(old == null) {
			if(!StringUtils.isEmpty(user.getId()) && StringUtils.isEmpty(user.getPassword())) {
				//如果页面上传过来的id不为空  然后密码为空
				//则使用原来的旧的密码
				//如果在数据库能找到这个id,就还是使用原来的id，否则就设置为空
				old = userDao.findById(user.getId()).orElse(null);
				//使用原来的密码
				user.setPassword(old.getPassword());
			}else {
				//使用新的密码
				String password = this.passwordEncoder.encode(user.getPassword());
				user.setPassword(password);
			}
			 //old不为空，表示没有被占用，直接保存
			userDao.save(user);
		}else {
			// 可能是修改的时候，登录名没有改变
			if(user.getId() != null && user.getId().equals(old.getId()) ) {
				/*用户的id不为空并且从数据里面可以查到这个用户的id，说明是同一个用户，表示只是修改，不是新增*/
				// 有id表示修改，并且页面传入的id跟数据库查询得到的id相同。
				// 此时表示：页面的登录名和数据库的登录名相同，但是属于同一个用户。
				if(StringUtils.isEmpty(user.getPassword())) {
					
					//这一步是为了在做修改的时候，因为不修改密码，然后保存的时候密码会自动设置为空的bug
					//密码为空，则使用原来的密码
					user.setPassword(old.getPassword());
				}else {
					//使用新的密码
					String password = this.passwordEncoder.encode(user.getPassword());
					user.setPassword(password);
				}
				
				this.userDao.save(user);
			}else {
				// 此时表示：页面的登录名和数据库登录名相同，但是不属于同一个用户，登录名被占用！
				// 如果user的id为null的时候，表示新增，但是数据库有个同名的登录名。
				throw new IllegalArgumentException("登录名重复");
			}
		}
	}

	@Override
	public Page<User> findUsers(String keyword, Integer number) {
		/*//如果关键字为空，就直接把关键字设置为空
		if(StringUtils.isEmpty(keyword)) {
			keyword = null;
		}
		
		//分页条件 Pageable为Spring-boot提供，并且封装好的，直接使用
		Pageable pageable = PageRequest.of(number, 4);
		
		Page<User> page;
		
		if(keyword == null) {
			//如果关键字为空，表示分页查询所有的数据
			page = userDao.findAll(pageable);
		}else {
			//根据姓名查询，为前后模糊查询
			page = userDao.findByNameContaining(keyword, pageable);
		}*/
		Page<User> page = this.findUsers(keyword, number,4);
		return page;
	}

	@Override
	public User findUserById(String id) {
		
		//orElse :三目运算符，如果没有从数据库找到对象，那么就返回orElse的参数，就是空，为null。
		return userDao.findById(id).orElse(null);
	}

	@Override
	@Transactional //每次激活，都要增加两个月的时间，	
	public void active(String id) {
		//先调用持久层查询该id的用户,必须要查询到才能进行激活操作
		User user = this.userDao.findById(id).orElse(null);
		if(user != null) {
			//点击激活操作，增加两个月的时间，并把状态该为正常
			user.setExpiredTime(getExpiredTime());
			user.setStatus(User.Status.NORMAL);
		}
	}
	
	//增加两个月写在方法里面，用到时直接调用方法即可     2个月不等于60天！！！
	private Date getExpiredTime() {
		//加上2个月
		Calendar cal = Calendar.getInstance();
		
		int month = cal.get(Calendar.MONTH);//获取月份
		month += 2;
		cal.set(Calendar.MONTH, month); // 把修改的月份设置回去！
		
		//2个月之后的时间
		Date time = cal.getTime();
		return time;
	}

	@Override
	@Transactional
	public void disable(String id){
		User user = this.userDao.findById(id).orElse(null);
		if(user != null) {
			user.setStatus(User.Status.DISABLE);
		}
	}

	public static void main(String[] args) {
		Date date = new Date();
		System.out.println("当前时间：" + date);
		//加上2个月
		Calendar cal = Calendar.getInstance();
		System.out.println("--->" + cal);
		
		int month = cal.get(Calendar.MONTH);//获取月份
		month += 2;
		cal.set(Calendar.MONTH, month); // 把修改的月份设置回去！
		
		//2个月之后的时间
		Date time = cal.getTime();
		System.out.println("2个月之后的时间：" + time);
	}
	@Override
	public Optional<User> findByLoginName(String loginName) {
		User user = this.userDao.findByLoginName(loginName);
		// 把查询到的User转换为Optional
		Optional<User> op = Optional.ofNullable(user);
		return op;
	}

	@Override
	public List<User> findUsers(String keyword) {
		Page<User> page = this.findUsers(keyword,0, 50);
		return page.getContent();
	}

	private Page<User> findUsers(String keyword, Integer number, Integer size) {
		if(StringUtils.isEmpty(keyword)) {
			keyword = null;
		}
		//分页条件
		Pageable pageable = PageRequest.of(number, size);
		
		Page<User> page;
		if(keyword == null) {
			// 分页查询所有数据
			page = userDao.findAll(pageable);
		}else {
			// 根据姓名查询，前后模糊查询
			page = userDao.findByNameContaining(keyword, pageable);
		}
		return page;
	}
}
