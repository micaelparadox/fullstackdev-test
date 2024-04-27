package com.micaeltest.QIMA.fullstackdev.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.micaeltest.QIMA.fullstackdev.service.CustomUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler;

	@Autowired
	private CustomUserDetailService customUserDetailService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http
	        .authorizeRequests()
	            .antMatchers("/", "/shop/**", "/forgotpassword", "/register", "/h2-console/**").permitAll()
	            // .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
	            .anyRequest().authenticated()
	            .and()
	        .formLogin()
	            .loginPage("/login")
	            .permitAll()
	            .failureUrl("/login?error=true")
	            .defaultSuccessUrl("/")
	            .usernameParameter("email")
	            .passwordParameter("password")
	            .and()
	        .oauth2Login()
	            .loginPage("/login")
	            .successHandler(googleOAuth2SuccessHandler)
	            .and()
	        .logout()
	            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	            .logoutSuccessUrl("/login")
	            .invalidateHttpSession(true)
	            .deleteCookies("JSESSIONID")
	            .and()
	        .exceptionHandling()
	            .and()
	        .csrf().disable();

	    // this is for h2-console
	    http.headers().frameOptions().disable();
	}


	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailService);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.httpFirewall(allowSemicolonHttpFirewall());
		web.ignoring().antMatchers("/resources/**", "/static/**", "/images/**", "/productImages/**", "/css/**",
				"/js/**");
	}

	@Bean
	public HttpFirewall allowSemicolonHttpFirewall() {
		StrictHttpFirewall firewall = new StrictHttpFirewall();
		firewall.setAllowSemicolon(true);
		return firewall;
	}
}