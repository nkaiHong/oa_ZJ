package org.fkjava.menu.service;

import java.util.List;
import java.util.Set;

import org.fkjava.common.data.domain.Result;
import org.fkjava.menu.domain.Menu;

public interface MenuService {

	void save(Menu menu);

	List<Menu> findTopMenus();
	

	Result move(String id, String targetId, String moveType);

	Result delete(String id);

	//List<Menu> findMyMenus();

	List<Menu> findMyMenus(String string);
	
	Set<String> findMyUrls(String string);

}
