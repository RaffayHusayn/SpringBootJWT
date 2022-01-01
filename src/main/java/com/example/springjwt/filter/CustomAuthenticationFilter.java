package com.example.springjwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    public CustomAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("the username : {} password :{} ", username, password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //Using the whole package name just to differentiate User class from Spring Security to my own user Class
        //The Casting user class is actually my own user class
        //Principal is the authenticated user
        System.out.println("========================================================authentication success");
        org.springframework.security.core.userdetails.User user = (User) authResult.getPrincipal();

        log.info("authentication successful, user: {} password: {}", user.getUsername(), user.getPassword());
        //this Class is from JWT dependency
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes(StandardCharsets.UTF_8));
        /*
        Access Token which expires quickly- 10 mins in our case
         */
        String accessToken = JWT.create()
                                .withSubject(user.getUsername())
                                .withExpiresAt(new Date(System.currentTimeMillis()+ 100*60*1000)) //expires in 100 mins
                                .withIssuer(request.getRequestURL().toString())
                //:: is called a method reference introduced in java8, getAuthority is a method that returns a String
                                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                                .sign(algorithm);

        /*
        Refresh Token which is used to generate another Access Token without Authentication again - Expires in 24 hours in our case
         */
        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+ 1000*60*60*24))//expires in 24 hours
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        /*
        Sending the JWT tokens in the response Header
         */
//        response.setHeader("access_token", accessToken);
//        response.setHeader("refresh_token", refreshToken);
        /*
        Sending JWT tokens in the response Body as JSON objects
         */
        Map<String , String> token = new HashMap<>();
        token.put("access_token",accessToken);
        token.put("refresh_token", refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), token);
    }
}
