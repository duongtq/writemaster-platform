package com.writemaster.platform.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  private final UserDetailsService jwtUserDetailsService;

  private final JwtRequestFilter jwtRequestFilter;

  public WebSecurityConfig(
          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
          UserDetailsService jwtUserDetailsService,
          JwtRequestFilter jwtRequestFilter) {
    this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    this.jwtUserDetailsService = jwtUserDetailsService;
    this.jwtRequestFilter = jwtRequestFilter;
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    // Configure AuthenticationManager so that it knows from where to load user for
    // matching credentials
    // Use BCryptPasswordEncoder
    auth
            .userDetailsService(jwtUserDetailsService)
            .passwordEncoder(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.cors();
    httpSecurity.csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/v1/authenticate/**").permitAll()
            .antMatchers("/api/v1/authors/register").permitAll()
            .antMatchers("/api/v1/posts/**").permitAll()
            .antMatchers("/api/v1/admin/**").hasAnyRole(Roles.ADMIN)
            .antMatchers("/api/v1/likes/**").hasAnyRole(Roles.AUTHOR)
            .antMatchers("/api/v1/comments/**").hasAnyRole(Roles.AUTHOR)
            .antMatchers("/api/v1/shares/**").hasAnyRole(Roles.AUTHOR)
            .anyRequest()
            .authenticated()
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    // Add a filter to validate the tokens with every request
    httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers("/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**");
  }
}
