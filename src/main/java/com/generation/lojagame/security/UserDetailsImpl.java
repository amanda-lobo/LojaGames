package com.generation.lojagame.security;

import java.util.Collection;
import java.util.List;

import com.generation.lojagame.model.Usuario;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails { //classe para controle interno
	private static final long serialVersionUID = 1L;

	private String userName;
	private String password;
	private List<GrantedAuthority> authorities;

	//construtor de classe
	public UserDetailsImpl(Usuario usuario)
	{
		this.userName = usuario.getEmailUsuario();
		this.password = usuario.getSenhaUsuario();
	}
	
	//construtor de classe vazio
	public UserDetailsImpl() {}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() 
	{
		return authorities;
	}

	@Override
	public String getPassword() 
	{
		return password;
	}

	@Override
	public String getUsername() 
	{

		return userName;
	}

	@Override
	public boolean isAccountNonExpired() 
	{
		return true;
	}

	@Override
	public boolean isAccountNonLocked() 
	{
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() 
	{
		return true;
	}

	@Override
	public boolean isEnabled() 
	{
		return true;
	}
	//interface nativa
}