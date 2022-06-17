package com.example.stocktradingsystem.handler;

import com.example.stocktradingsystem.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String redirectURL = request.getContextPath();

        if (userDetails.hasRole("ROLE_ADMIN")) {
            redirectURL = "admin";
        } else if (userDetails.hasRole("ROLE_USER")) {
            redirectURL = "stocks";
        }
        response.sendRedirect(redirectURL);
    }
}
