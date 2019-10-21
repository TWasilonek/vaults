package com.tomaszwasilonek.vaults.ws.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.tomaszwasilonek.vaults.ws.service.UserService;
import com.tomaszwasilonek.vaults.ws.service.impl.AuthenticationFilter;
import com.tomaszwasilonek.vaults.ws.service.impl.AuthorizationFilter;
import com.tomaszwasilonek.vaults.ws.service.impl.SecurityConstants;


@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
	
	private final UserService userDetailsService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public WebSecurity(UserService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
			// no authentication needed for POST requests to '/users'
			.antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
			.permitAll()
			// For all other requests authentication is required
			.anyRequest().authenticated()
			.and()
			// add a custom authentication mechanism
			.addFilter(getAuthenticationFilter())
			// add a custom authorization mechanism
			.addFilter(new AuthorizationFilter(authenticationManager()))
			// don't create a session and don't cache the JWT token
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}
	
	// Set a custom URI for the login.
	// By default SpringSecurity sets it to "/login"
	public AuthenticationFilter getAuthenticationFilter() throws Exception {
		final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
		filter.setFilterProcessesUrl("/users/login");
		return filter;
	}
	
}
