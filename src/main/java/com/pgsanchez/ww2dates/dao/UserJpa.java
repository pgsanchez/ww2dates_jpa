package com.pgsanchez.ww2dates.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgsanchez.ww2dates.model.MyUser;

public interface UserJpa extends JpaRepository<MyUser, String> {
	MyUser findByUsername(String username);
}
