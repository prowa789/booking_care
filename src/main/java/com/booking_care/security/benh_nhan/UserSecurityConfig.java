package com.booking_care.security.benh_nhan;

import com.booking_care.model.CustomOAuth2User;
import com.booking_care.model.TaiKhoan;
import com.booking_care.repository.TaiKhoanRepository;
import com.booking_care.service.BenhNhanGoogleService;
import com.booking_care.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Configuration
@Order(2)
public class UserSecurityConfig extends WebSecurityConfigurerAdapter{

//    @Autowired
//    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private TaiKhoanRepository taiKhoanRepo;
    @Autowired
    private CustomOAuth2UserService oauthUserService;
    @Autowired
    private BenhNhanGoogleService benhNhanGoogleService;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider2(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Override
    public void configure(HttpSecurity http) throws Exception{

        http.csrf().disable();
        http.authorizeRequests().antMatchers("/","/oauth/**").permitAll();
        http.authenticationProvider(authenticationProvider2());
        http.authorizeRequests().antMatchers("/js/**","/css/**","/dang-nhap").permitAll();

        http
                .formLogin()
                .loginPage("/dang-nhap")
                .loginProcessingUrl("/dang-nhap")
                .defaultSuccessUrl("/",true)
                .permitAll()
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                .and()
                    .exceptionHandling().accessDeniedPage("/403")
                .and()
                .oauth2Login()
                .loginPage("/dang-nhap")
                .failureUrl("/login")
                .userInfoEndpoint()
                .userService(oauthUserService)
                .and()
                .successHandler(new AuthenticationSuccessHandler() {

            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                System.out.println(authentication.getPrincipal());
                CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
                TaiKhoan taiKhoan = taiKhoanRepo.findByUsername(oauthUser.getEmail());
                if(taiKhoan == null)
                    benhNhanGoogleService.processOAuthPostLogin(oauthUser);
                UserDetails userDetails = userDetailsService().loadUserByUsername(oauthUser.getEmail());
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                response.sendRedirect("/profile");
            }
        });

        http.rememberMe().rememberMeParameter("benhNhanId").rememberMeCookieName("benhNhanId").key("uniqueAndSecret").tokenValiditySeconds(1296000)
        .userDetailsService(userDetailsService());


    }


}
