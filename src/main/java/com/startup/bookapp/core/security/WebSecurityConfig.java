package com.startup.bookapp.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.httpBasic().and()
			.authorizeRequests()
			.antMatchers("/","/currentuser","/book/buy").permitAll().anyRequest()
			.authenticated().and().addFilterAfter(new CsrfHeaderFilter(),
				CsrfFilter.class).csrf().csrfTokenRepository(csrfTokenRepository());
		
//		http.httpBasic().and().authorizeRequests().antMatchers("/","/currentuser","/book/buy","/book").permitAll().anyRequest().authenticated()
//		.and().csrf().disable();

	}

	private CsrfTokenRepository csrfTokenRepository() {
		CustomizedHttpSessionCsrfTokenRepository repository = new CustomizedHttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");

		return repository;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Override
	protected UserDetailsService userDetailsService() {
		return userDetailsService;
	}
	
	@Bean
    public HttpSessionStrategy httpSessionStrategy() {
        return new HeaderHttpSessionStrategy();
    }

}
