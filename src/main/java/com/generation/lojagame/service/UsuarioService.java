package com.generation.lojagame.service;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.lojagame.model.Usuario;
import com.generation.lojagame.model.UsuarioLogin;
import com.generation.lojagame.repository.UsuarioRepository;

//classe service é feito para as regras de negócio

@Service //notação da classe serviço
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	// Cadastrar usuario
	public Optional<Usuario> cadastrarUsuario(Usuario usuario) 
	{
		if (usuarioRepository.findByemailUsuario(usuario.getEmailUsuario()).isPresent())
			return Optional.empty();

		if (calcularIdade(usuario.getDataNascimento()) < 18)
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, "Usuário é menor de 18 anos", null);

		usuario.setSenhaUsuario(criptografarSenha(usuario.getSenhaUsuario()));

		return Optional.ofNullable(usuarioRepository.save(usuario));
	}

	// Atualizar usuario
	public Optional<Usuario> atualizarUsuario(Usuario usuario) 
	{
		if(usuarioRepository.findById(usuario.getIdUsuario()).isPresent())
		{	
			Optional<Usuario> buscaUsuario = usuarioRepository.findByemailUsuario(usuario.getEmailUsuario());
			
			if ( (buscaUsuario.isPresent()) && ( buscaUsuario.get().getIdUsuario() != usuario.getIdUsuario()))
				throw new ResponseStatusException(
						HttpStatus.BAD_REQUEST, "Usuário já existe!", null);
			
			if (calcularIdade(usuario.getDataNascimento()) < 18)
				throw new ResponseStatusException(
						HttpStatus.BAD_REQUEST, "Usuário é menor de 18 anos", null);
			
			usuario.setSenhaUsuario(criptografarSenha(usuario.getSenhaUsuario()));

			return Optional.ofNullable(usuarioRepository.save(usuario));	
		}
			return Optional.empty();	
	}
	
	// Autenticar usuario
	public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin)
	{	
		Optional<Usuario> buscaUsuario = usuarioRepository.findByemailUsuario(usuarioLogin.get().getEmailUsuario());
		
		if(buscaUsuario.isPresent()) 
		{		
			if( compararSenhas(usuarioLogin.get().getSenhaUsuario(), buscaUsuario.get().getSenhaUsuario()) )
			{	
				usuarioLogin.get().setIdUsuario(buscaUsuario.get().getIdUsuario());
				usuarioLogin.get().setNomeUsuario(buscaUsuario.get().getNomeUsuario());
				usuarioLogin.get().setFoto(buscaUsuario.get().getFoto());
				usuarioLogin.get().setDataNascimento(buscaUsuario.get().getDataNascimento());
				usuarioLogin.get().setToken(gerarBasicToken(usuarioLogin.get().getEmailUsuario(), usuarioLogin.get().getSenhaUsuario()));
				usuarioLogin.get().setSenhaUsuario(buscaUsuario.get().getSenhaUsuario());
				
				return usuarioLogin;
			}
		}
		
		return Optional.empty();
	}
	
	private int calcularIdade(LocalDate dataNascimento)
	{
		return Period.between(dataNascimento, LocalDate.now()).getYears();
	}

	private String criptografarSenha(String senha)
	{
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return encoder.encode(senha);
	}
	
	private boolean compararSenhas(String senhaDigitada, String senhaBanco)
	{
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return encoder.matches(senhaDigitada, senhaBanco);
	}
	
	private String gerarBasicToken(String usuario, String senha)
	{	
		String token = usuario + ":" + senha;
		byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII"))); 
		
		return "Basic " + new String(tokenBase64);
	}
}