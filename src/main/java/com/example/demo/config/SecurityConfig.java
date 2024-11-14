package com.example.demo.config;

import com.example.demo.auth.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public JwtFilter authFilter() {
        return new JwtFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // Enable CORS and disable CSRF
                .cors().and()
                .csrf().disable()

                // Disable HTTP Basic authentication
                .httpBasic().disable()

                // Configure endpoint authorization
                .authorizeRequests()

                // Allow anonymous access to the signup endpoint
                .antMatchers(HttpMethod.POST, "/api/user/signup").permitAll()

                // Deny all OPTIONS requests
                .antMatchers(HttpMethod.OPTIONS).denyAll()

                // Permit all other requests (should be restricted for production)
                .anyRequest().permitAll()

                .and()

                // Stateless session management
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()

                // Add JWT filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
