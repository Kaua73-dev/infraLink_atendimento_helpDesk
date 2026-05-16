package com.infraLink.API.config;


import com.infraLink.API.model.repository.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenConfig tokenConfig;
    private final UserRepository userRepository;

    public SecurityFilter(TokenConfig tokenConfig, UserRepository userRepository) {
        this.tokenConfig = tokenConfig;
        this.userRepository = userRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = recoverToken(request);

        try {

            String email = tokenConfig.validateToken(token);

            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                var userOptional = userRepository.findByEmail(email);

                if(userOptional.isPresent()){
                    UserDetails user = userOptional.get();

                    var authenticate = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authenticate);

                }
            }




        } catch (Exception e){

        }

        filterChain.doFilter(request, response);

    }


    private String recoverToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");

        if(header == null || !header.startsWith("Bearer ")){
            return null;
        }

        return header.replace("Bearer", "");

    }


}
