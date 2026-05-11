package com.example.demo;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class ClienteUserDetails implements UserDetails {

	private Cliente cliente;

	public ClienteUserDetails(Cliente cliente) {
		//super();
		this.cliente = cliente;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(cliente.getRole()));
	}

	@Override
	public String getPassword() {
		return cliente.getPassword();
	}

	@Override
	public String getUsername() {
		return cliente.getEmail();
	}

}
