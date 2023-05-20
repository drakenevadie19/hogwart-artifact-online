package edu.tcu.cs.hogwartsartifactsonline.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/*
* This class handles unsuccessful JWT authentication
* */
@Component
public class CustomBearerTokenAuthentication implements AuthenticationEntryPoint {
    private final HandlerExceptionResolver resolver;

    // we need exact bean handlerExceptionResolver
    public CustomBearerTokenAuthentication(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // exception now can be hanlde in exception handler advice
        this.resolver.resolveException(request, response, null, authException);
    }
}
