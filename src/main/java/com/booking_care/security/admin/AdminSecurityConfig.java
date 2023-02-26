package com.booking_care.security.admin;

import com.booking_care.security.bac_sy.BacSyDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(1)
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter{
    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public UserDetailsService adminDetailsService() {
        return new AdminDetailsServiceImpl();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider1() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(adminDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/").permitAll();
        http.authenticationProvider(authenticationProvider1());
        http.authorizeRequests().antMatchers("/js/**","/css/**","/admin/login").permitAll();
        http.antMatcher("/admin/**").authorizeRequests().anyRequest().hasAuthority("ADMIN");

        http
                .formLogin()
                .loginPage("/admin/login")
                .usernameParameter("username").passwordParameter("password")
                .loginProcessingUrl("/admin/login")
                .defaultSuccessUrl("/admin/home",true)
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/admin/login").and()
                .exceptionHandling().accessDeniedPage("/403");

        http.rememberMe().rememberMeParameter("adminId")
                .rememberMeCookieName("adminId").key("aaaaa")
                .tokenValiditySeconds(1296000).userDetailsService(adminDetailsService());

    }
}