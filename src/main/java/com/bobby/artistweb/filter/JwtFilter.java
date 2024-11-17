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
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

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
        String authHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                jwtToken = authHeader.substring(7);
                username = this.jwtService.extractAppName(jwtToken);
            } catch(SignatureException e) {
                System.out.println("JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.");
                this.returnTokenInvalidResponse(response, "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.");
            } catch(ExpiredJwtException e) {
                System.out.println("JWT is expired.");
                this.returnTokenInvalidResponse(response, "JWT is expired.");
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
                }
            } catch (SignatureException e) {
                System.out.println("Invalid JWT signature");
                this.returnTokenInvalidResponse(response, "Invalid JWT signature.");
            }
        }

        if ((authHeader == null || !authHeader.startsWith("Bearer ")) && request.getMethod().equalsIgnoreCase("OPTIONS") == false) { //Allow options requests to pass
            if(request.getRequestURI().endsWith("users/login") == false) {
                this.returnTokenInvalidResponse(response, "In order to access, please carry a valid token!");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void returnTokenInvalidResponse(HttpServletResponse response, String message) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "false");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = null;
        OutputStreamWriter osw = null;

        try {
            osw = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
            writer = new PrintWriter(osw, true);
            writer.write(message);
            writer.flush();
            writer.close();
            osw.close();
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (null != writer) {
                writer.close();
            }
            if (null != osw) {
                osw.close();
            }
        }
    }
}
