package com.pgsanchez.ww2dates.dao;

import com.pgsanchez.ww2dates.model.MyUser;

public interface UserDao {
	public MyUser findByUsername(String username);
}
