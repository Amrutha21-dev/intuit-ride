package com.intuit.demo.filter;


import com.intuit.demo.constant.AppConstant;
import com.intuit.demo.enumeration.UserType;
import com.intuit.demo.exception.UnauthorisedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class AdminAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain)
            throws IOException, ServletException {
        String role = request.getHeader(AppConstant.X_ROLE);
        try {
            log.info("Authorizing request for role");
            if(!UserType.ADMIN.getValue().equals(role)){
                throw new IllegalArgumentException();
            }
            log.info("Authorised request for role");
            filterChain.doFilter(request, response);
        }
        catch (IllegalArgumentException e){
            log.error("Unauthorised request with invalid role"+request.getRequestURI(),e);
            throw new UnauthorisedException("Unauthorised request with invalid role");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return (
                path.equals("/v1/admin/register")
        );
    }
}
