package com.simplescrumpoker.config;

import com.simplescrumpoker.http.controller.SignInController;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SignInController signInController;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .disable()
                .authorizeHttpRequests()
                    .antMatchers("/**").permitAll()
                    .antMatchers("/css/**", "/js/**", "/image/**").permitAll()
                    .antMatchers("/signin").permitAll()
                    .antMatchers("/signup").permitAll()
                    .antMatchers("/users/password/remind").permitAll()
                    .antMatchers("/users/password/set/**").permitAll()
                    .antMatchers("/rooms/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/rooms/enter", "/rooms/enter/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/rooms/enter", "/rooms/enter/**").permitAll()
                    .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                    .anyRequest()
                    .authenticated()
                    .and()
                .formLogin()
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .loginPage("/signin")
                    .defaultSuccessUrl("/")
                    .successHandler(signInController)
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
