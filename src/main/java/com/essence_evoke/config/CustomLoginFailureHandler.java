package com.essence_evoke.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws

            IOException, ServletException {

        String errorMessage = "Invalid email or password";

        // Check if user is disabled (not confirmed)
        if (exception instanceof DisabledException) {
            errorMessage = "Your account is not confirmed. Please check your email.";
        }

        // Put the error message as a request attribute instead of query param
        request.setAttribute("loginErrorMessage", errorMessage);
        response.sendRedirect("/login?errorMessage=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));
    }
}
