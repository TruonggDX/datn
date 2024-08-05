//package edu.hunre.course_management.security.oauth2;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//@Component
//public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        CustomerOAuth2 customerOAuth2 = (CustomerOAuth2) authentication.getPrincipal();
//        String email = customerOAuth2.getEmail();
//        System.out.println("email : "+email);
//        super.onAuthenticationSuccess(request, response, authentication);
//    }
//}
