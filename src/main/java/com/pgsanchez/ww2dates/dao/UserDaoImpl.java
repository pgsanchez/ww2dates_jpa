package com.pgsanchez.ww2dates.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pgsanchez.ww2dates.model.MyUser;



@Repository
public class UserDaoImpl implements UserDao{

	@Autowired
	UserJpa userJpa;
	
	public MyUser findByUsername(String username) {
		return userJpa.findByUsername(username);
	}
}
