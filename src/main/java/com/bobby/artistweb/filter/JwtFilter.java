package com.bobby.artistweb.filter;


import com.bobby.artistweb.service.ApplicationDetailsService;
import com.bobby.artistweb.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This filter used to validate the validity of token.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean isValidToken = false;
        String authHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                jwtToken = authHeader.substring(7);
                username = this.jwtService.extractAppName(jwtToken);
            } catch(SignatureException e) {
                isValidToken = false;
                System.out.println("JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.");
            } catch(ExpiredJwtException e) {
                isValidToken = false;
                System.out.println("JWT is expired.");
            }
        }
        // validate jwt token
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.context.getBean(ApplicationDetailsService.class).loadUserByUsername(username);

            try {
                //success
                if(this.jwtService.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                   isValidToken = true;
                }
            } catch (SignatureException e) {
                isValidToken = false;
                System.out.println("Invalid JWT signature");
            }

        }
        request.setAttribute("isValidToken", isValidToken);
        filterChain.doFilter(request, response);
    }
}
