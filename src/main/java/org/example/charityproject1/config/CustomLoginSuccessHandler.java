package org.example.charityproject1.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.charityproject1.security.OrganisationUserDetails;
import org.example.charityproject1.security.RegularUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomLoginSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof OrganisationUserDetails) {
            logger.debug("Organisation logged in, redirecting to /organisations/dashboard");
            response.sendRedirect("/organisations/dashboard");
        } else if (principal instanceof RegularUserDetails) {
            logger.debug("Regular user logged in, redirecting to /dashboard");
            response.sendRedirect("/dashboard");
        } else {
            logger.debug("Unknown user type, redirecting to /login?error=unknown");
            response.sendRedirect("/login?error=unknown");
        }
    }
}
