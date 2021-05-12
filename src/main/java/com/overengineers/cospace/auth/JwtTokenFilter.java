package com.overengineers.cospace.auth;

import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.service.CustomUserDetailsManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final CustomUserDetailsManager userDetailsManager;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = httpServletRequest.getHeader("Authorization");

        String username = null;
        String token = null;

        if(authHeader != null && authHeader.contains("Bearer")){
            token = authHeader.substring(7);
            try{
                username = TokenManager.getUsernameFromToken(token);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        if(username != null && token != null
                && SecurityContextHolder.getContext().getAuthentication() == null){
            if(TokenManager.tokenValidate(token)){
                Member member = (Member) userDetailsManager.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken upassToken = new UsernamePasswordAuthenticationToken(username, null, member.getAuthorities());
                upassToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(upassToken);
            }

        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}
