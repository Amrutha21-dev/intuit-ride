package com.intuit.demo.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.demo.constant.AppConstant;
import com.intuit.demo.constant.ErrorConstant;
import com.intuit.demo.exception.UnauthorisedException;
import com.intuit.demo.model.response.APIResponse;
import com.intuit.demo.model.response.EmptyResponse;
import com.intuit.demo.util.APIResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class BaseOncePerRequestFilter extends OncePerRequestFilter{

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        }
        catch (UnauthorisedException e){
            log.error("Exception occurred",e);
            response.reset();
            APIResponse<EmptyResponse> apiResponse = APIResponseUtil.createFailureUnauthorisedAPIResponse(e.getMessage());
            String apiResponseString = objectMapper.writeValueAsString(apiResponse);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(AppConstant.APPLICATION_JSON);
            response.setContentLength(apiResponseString.length());
            response.getWriter().write(apiResponseString);
        }
        catch (Exception e){
            log.error("Exception occurred",e);
            response.reset();
            APIResponse<EmptyResponse> apiResponse = APIResponseUtil.createFailureInternalErrorResponse(ErrorConstant.INTERNAL_ERROR);
            String apiResponseString = objectMapper.writeValueAsString(apiResponse);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType(AppConstant.APPLICATION_JSON);
            response.setContentLength(apiResponseString.length());
            response.getWriter().write(apiResponseString);
        }
    }
}
