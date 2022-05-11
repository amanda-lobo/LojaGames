package com.generation.lojagame.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.generation.lojagame.model.Usuario;
import com.generation.lojagame.repository.UsuarioRepository;

// Implementa a interface UserDetailsService, responsável pela recuperação dos dados do usuario no DB e cpnverte em objeto na classe UserDetailsImpl

@Service //notação de classe de serviço
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException
	{
		Optional<Usuario> usuario = usuarioRepository.findByemailUsuario(userName); //inserindo no objeto
		usuario.orElseThrow(() -> new UsernameNotFoundException(userName + "not found.")); //lambda -> caso der erro
		
		return usuario.map(UserDetailsImpl :: new).get(); //vai entregar um novo UserDetails, por ser o option preciso do get pra extrair o que tem dentro do objeto
	}
}
