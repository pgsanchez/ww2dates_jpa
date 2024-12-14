package com.pgsanchez.ww2dates.securingweb;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pgsanchez.ww2dates.model.MyUser;

public class MyUserDetails implements UserDetails {

	private MyUser myUser;
	
	
	public MyUserDetails(MyUser myUser) {
		this.myUser = myUser;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(myUser.getAuthorities());
        return Arrays.asList(authority);
	}

	@Override
	public String getPassword() {
		return myUser.getPassword();
	}

	@Override
	public String getUsername() {
		return myUser.getUsername();
	}

}
