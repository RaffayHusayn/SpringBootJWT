package com.example.springjwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class CustomAuthorizationFilter extends OncePerRequestFilter {
    /*
     * OncePerRequest filter intercepts every request that comes in

     */


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //don't apply this filter to login path because that should be accessible to everyone
        if(request.getServletPath().equals("/login")){
            //just letting the request and response pass through to the other filters in the chain
            filterChain.doFilter(request, response);
        }else{
//            AUTHORIZATION is simply a String "authorization" in the backend
//            it is defined like this ::: public static final String AUTHORIZATION = "Authorization";
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
               try{
                   //basically just removing the starting substring to get rid of Bearer infront of the JWT token
                   String token = authorizationHeader.substring("Bearer ".length());

                   //redoing it here to get the same algorithm to decode the Token that we used to create it with the same secret and algorithm
                   Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                   JWTVerifier verifier = JWT.require(algorithm).build();
                   DecodedJWT decodedJWT = verifier.verify(token);

                   //getting the contents out of the Token ie username and it's authorities/roles
                   String username = decodedJWT.getSubject();
                   String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

                   Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                   //converting the Arrays of String Roles to SimpleGrantedAuthorities which can be used by Spring
                   stream(roles).forEach(role -> {
                       authorities.add(new SimpleGrantedAuthority(role));

                   });
                   UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                   SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                   filterChain.doFilter(request, response);

               }catch(Exception e){

               }

            }


        }




    }
}