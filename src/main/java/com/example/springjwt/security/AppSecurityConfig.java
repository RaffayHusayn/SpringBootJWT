package com.example.springjwt.security;
import com.example.springjwt.filter.CustomAuthenticationFilter;
import com.example.springjwt.filter.CustomAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
public class AppSecurityConfig extends WebSecurityConfigurerAdapter{

    private final PasswordEncoder bCryptPasswordEncoder;
    private final UserDetailsService userDetailsService;

    /*
    constructor for the final fields
     */
    public AppSecurityConfig(UserDetailsService userDetailsService, PasswordEncoder bCryptPasswordEncoder){
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);

    }

    /*
    WebSecurityConfigurerAdapter already have a authenticationManagerBean() method that returns a Authentication manager
    so we can just use that in our CustomAuthenticationFilter constructor which requires an AuthenticationManager
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        //so that we can create new users using POST requests from anywhere and we aren't stopped by csrf,
        //there should be a better way to do it but for now it is fine.
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        //everyone has access to login endpoint which is provided by Spring and not defined by us,
        // we can obviously change the name of this and everything later but let's leave it for now
        /*
        rules that I want:

        1. Managers and Super_Admins can create new users
        2. ROLE_ADMIN can delete users
        3. ROLE_USER and ROLE_ADMIN can get users or one individual user
        4. Everyone can login
        5. Everyone can get a new access JWT token using their refresh token

         */
        http.authorizeRequests()
                .antMatchers("/login/**", "/token/refresh/**").permitAll() //everyone is allowed to login and refresh token, No need for authentication or any assigned roles(Authentication in this case is done using JWT Authorization token
                .antMatchers("/user/save").hasAnyAuthority( "ROLE_MANAGER", "ROLE_SUPER_ADMIN") // manager and super admin can create new users
                .antMatchers( "/delete/user/**").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers( "/users","/user/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .anyRequest().denyAll();

        //       Adding filters
        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));
        // AddFilterBefore -> means that use this filter before using any other filters
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}