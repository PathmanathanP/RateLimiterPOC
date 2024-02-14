package com.ratelimiter.poc.interceptor;

import com.ratelimiter.poc.utils.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

public class RateLimitInterceptor implements HandlerInterceptor {
    private static final RateLimiter rateLimiter = new RateLimiter(10, 60000);
    @Value("${auth.uri.whitelist:}")
    public String[] whiteListedEndpoints;

    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        if (!isWhitelistedEndpoint(requestURI)) {
            return true;
        }

        String ipAddress = request.getRemoteAddr();
        if (rateLimiter.allowRequest(ipAddress)) {
            return true;
        } else {
            response.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
            response.getWriter().write("Too many requests. Please try again later.");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest arg0,
                                HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
                           Object arg2, ModelAndView arg3) throws Exception {

    }

    private boolean isWhitelistedEndpoint(String endpoint) {
        for (String whitelistedEndpoint : whiteListedEndpoints) {
            if (whitelistedEndpoint.equals(endpoint)) {
                return true;
            }
        }
        return false;
    }
}

