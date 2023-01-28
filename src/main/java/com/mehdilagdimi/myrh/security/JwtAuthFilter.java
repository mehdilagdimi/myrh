package com.mehdilagdimi.myrh.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mehdilagdimi.myrh.model.Response;
import com.mehdilagdimi.myrh.model.entity.User;
import com.mehdilagdimi.myrh.service.UserService;
import com.mehdilagdimi.myrh.util.JwtHandler;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {


    private final JwtHandler jwtHandler;
    private final UserService userService;

    private final ObjectMapper mapper;

    //Autowired is optional here because only one constructor is defined
    public JwtAuthFilter(JwtHandler jwtHandler, UserService userService, ObjectMapper mapper) {
        this.jwtHandler = jwtHandler;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("AUTHORIZATION");
        final String userEmail;
        final String token;
        final boolean isTokenValid;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        //extract jwt, email and userrole
        token = authHeader.substring(7);
        userEmail = jwtHandler.extractUsername(token);
        String userRole = jwtHandler.extractRole(token);


        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            User userDetails = (User)userService.loadUserByUsername(userEmail);
            if(userDetails != null){
//                try{
                    isTokenValid = jwtHandler.validateToken(token, userDetails);
                    if(isTokenValid) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
//                }catch (ExpiredJwtException e){
////                    e.printStackTrace();
//                    System.out.println(" inside jwt except");
//                    Response responseModel = new Response(HttpStatus.UNAUTHORIZED, "JWT Expired");
////                    ResponseEntity<Response> responseToSend = new ResponseEntity<>(responseModel, HttpStatusCode.valueOf(response.getStatus()));
//
//                    response.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
//                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    mapper.writeValue(response.getWriter(), responseModel);
//                    return;
//                }
            }

        }
        filterChain.doFilter(request,response);
    }
}
