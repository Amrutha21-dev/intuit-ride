package com.intuit.demo.filter;


import com.intuit.demo.constant.AppConstant;
import com.intuit.demo.exception.UnauthorisedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class ApiKeyRequestFilter extends OncePerRequestFilter {

    @Value("${api-key}")
    private String apiKey;


    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain)
            throws IOException, ServletException {
        String key = request.getHeader("X-Api-Key".toLowerCase());
        log.info("Trying key: " + key);
        if(apiKey.equals(key)){
            log.info("Key is valid");
            HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(request);
            requestWrapper.addHeader(AppConstant.X_SOURCE_IP_ADDRESS, getIpFromHeader(requestWrapper));
            filterChain.doFilter(requestWrapper, response);
        }else{
            log.error("Key is invalid");
            throw new UnauthorisedException("Invalid API Key");
        }

    }

    private String getIpFromHeader(HttpServletRequest request){
        String ip = request.getRemoteAddr();
        String headerClientIp = request.getHeader("Client-IP");
        String headerXForwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasLength(headerClientIp)) {
            ip = headerClientIp;
        } else if (StringUtils.hasLength(headerXForwardedFor)) {
            ip = headerXForwardedFor;
        }
        return ip;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return (
                path.startsWith("/actuator")
                || "/v3/api-docs".equals(path)
                || "/v3/api-docs/swagger-config".equals(path)
                || "/swagger-ui/index.html".equals(path)
                || "/swagger-ui/swagger-initializer.js".equals(path)
                || "/swagger-ui/swagger-ui-bundle.js".equals(path)
                || "/swagger-ui/swagger-ui-standalone-preset.js".equals(path)
                || "/swagger-ui/index.css".equals(path)
                || "/swagger-ui/swagger-ui.css".equals(path)
        );
    }
}
