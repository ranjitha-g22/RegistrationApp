package com.registrationjwt.Config;

import com.registrationjwt.entity.Registration;
import com.registrationjwt.repository.RegistrationRepository;
import com.registrationjwt.service.JwtService;
import jakarta.servlet.FilterChain;
//import jakarta.servlet.Registration;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private RegistrationRepository registrationRepository;


    public JwtFilter(JwtService jwtService, RegistrationRepository registrationRepository) {
        this.jwtService = jwtService;
        this.registrationRepository = registrationRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token=request.getHeader("Authorization");
        System.out.println(token);
        if(token!=null &&token.startsWith("Bearer ")){
            String jwtToken=token.substring(7,token.length());
            System.out.println(jwtToken);
            String userName=jwtService.getUserName(jwtToken);
            System.out.println(userName);
            Optional<Registration> op=registrationRepository.findByUserName(userName);
            if(op.isPresent()){
                Registration reg=op.get();
                UsernamePasswordAuthenticationToken authenticationToken=new
                        UsernamePasswordAuthenticationToken(reg,null,null);
                authenticationToken.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        }
        filterChain.doFilter(request,response);
    }
}
