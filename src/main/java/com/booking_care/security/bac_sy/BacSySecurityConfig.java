package com.booking_care.security.bac_sy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.*;

@Configuration
@Order(0)
public class BacSySecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public UserDetailsService bacSyDetailsService() {
        return new BacSyDetailsServiceImpl();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider3() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(bacSyDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authenticationProvider(authenticationProvider3());
        http.authorizeRequests().antMatchers("/js/**","/css/**","/bacsy/login").permitAll();
//        http.antMatcher("/bacsy/**").authorizeRequests().anyRequest().hasAuthority("BAC_SY");
        http.antMatcher("/bacsy/**").authorizeRequests().anyRequest().permitAll();

        http
                .formLogin()
                .loginPage("/bacsy/login")
                .usernameParameter("username").passwordParameter("password")
                .loginProcessingUrl("/bacsy/login")
                .defaultSuccessUrl("/bacsy/home",true)
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/bacsy/logout")
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/bacsy/login").and()
                .exceptionHandling().accessDeniedPage("/403");

        http.rememberMe().rememberMeParameter("bacSyId")
                .rememberMeCookieName("bacSyId").key("aaaaa12123123")
                .tokenValiditySeconds(1296000).userDetailsService(bacSyDetailsService());
    }
}