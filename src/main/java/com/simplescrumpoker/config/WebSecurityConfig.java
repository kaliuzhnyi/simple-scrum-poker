package com.simplescrumpoker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .disable()
                .authorizeHttpRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/css/**", "/js/**", "/image/**").permitAll()
                    .antMatchers("/signin").permitAll()
                    .antMatchers("/signup").permitAll()
                    .antMatchers("/users/password/remind").permitAll()
                    .antMatchers("/users/password/set/**").permitAll()
                    .antMatchers("/rooms/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/rooms/enter", "/rooms/enter/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/rooms/enter", "/rooms/enter/**").permitAll()
                    .anyRequest()
                    .authenticated()
                    .and()
                .formLogin()
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .loginPage("/signin")
                    .defaultSuccessUrl("/")
                    .failureUrl("/signin")
                    .and()
//                .rememberMe()
//                    .key("uniqueAndSecret")
//                    .and()
                .logout()
                    .logoutUrl("/signout")
                    .logoutSuccessUrl("/");

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
