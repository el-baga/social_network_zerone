package com.skillbox.zerone.security;


import com.skillbox.zerone.repository.PersonRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtils jwtTokenUtils;
    private final PersonRepository personRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (token != null && jwtTokenUtils.validateAccessToken(token)
                && (personRepository.existsById(jwtTokenUtils.getUserIdFromToken(token)))
                && !isUserBlocked(jwtTokenUtils.getUserIdFromToken(token))) {
            setAuthenticationContext(token);

        }

        filterChain.doFilter(request, response);
    }

    private void setAuthenticationContext(String token) {
        Authentication authentication = jwtTokenUtils.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private boolean isUserBlocked(Long userId) {
        return personRepository.existsByIdAndIsBlocked(userId, true);
    }

}
