package edu.hunre.course_management.security.oauth2;

import edu.hunre.course_management.entity.CustomerEntity;
import edu.hunre.course_management.repository.CustomerRepository;
import edu.hunre.course_management.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomerOAuth2Service extends DefaultOAuth2UserService {
    @Autowired
    private ICustomerService customerService;



    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        return new CustomerOAuth2(super.loadUser(userRequest));

//        String username = oAuth2User.getAttribute("email"); // Hoặc thay bằng thuộc tính phù hợp
//
//        // Gọi phương thức để xử lý lưu thông tin người dùng
//        customerService.processOAuthPostLogin(username);
//
//
//        System.out.println("Request to load user");
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//        String email = oAuth2User.getAttribute("email");
//        // Hoặc thay bằng thuộc tính phù hợp
//        System.out.println("email : " + username);
//        // Gọi phương thức để xử lý lưu thông tin người dùng
//        customerService.processOAuthPostLogin(username);
//        return new CustomerOAuth2(oAuth2User);
    }
}
