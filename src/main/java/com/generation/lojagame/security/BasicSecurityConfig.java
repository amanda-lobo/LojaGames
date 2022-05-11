package com.generation.lojagame.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*classe de configuração, onde é configurada a criptografia utilizada na senha, o tipo de segurança (basic)
 *e os end point's que serão liberados para o acesso*/

@EnableWebSecurity //configuração
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDatailsService; //metodo da classe WebSecurityConfigurerAdapter
	
	@Override //sobrescrita de método
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.userDetailsService(userDatailsService);
		
		auth.inMemoryAuthentication()
		.withUser("root")
		.password(passwordEncoder().encode("root"))
		.authorities("ROLE_USER");
	}
	
	@Bean //transforma a intancia do metodo como um objeto gerenciado pelo spring, podendo ser injetado em qualquer classe e a qualquer momento que precisar, sem necessidade do @Autowired
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http.authorizeRequests()
		.antMatchers("/usuarios/logar").permitAll() //configuração que libera caminhos no controller para ter acesso sem precisar de um token - endpoint
		.antMatchers("/usuarios/cadastrar").permitAll()
		.antMatchers(HttpMethod.OPTIONS).permitAll()
		.anyRequest().authenticated() //todas as outras requisições deverão ser autenticadas -> no header deverá passar a chave
		.and().httpBasic() //padrão basic que irá gerar o token
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // sessionManagement() indica o tipo de seção que usaremos, sessionCreationPolicy criar uma politica, SessionCreationPolicy.STATELESS não guarda seções
		.and().cors()
		.and().csrf().disable(); //desabilitado
	}
}
