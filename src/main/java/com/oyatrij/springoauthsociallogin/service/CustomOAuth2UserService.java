package com.oyatrij.springoauthsociallogin.service;

import com.oyatrij.springoauthsociallogin.domain.User;
import com.oyatrij.springoauthsociallogin.dto.Role;
import com.oyatrij.springoauthsociallogin.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    UserRepository userRepository;

    @Autowired
    HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        //서비스 구분 naver, google, ...
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        String email;
        String name;
        Map<String, Object> response = oAuth2User.getAttributes();

        if(registrationId.equals("naver")) {
            Map<String, Object> hash = (Map<String, Object>) response.get("response");
            email = (String)hash.get("email");
            name = (String)hash.get("name");
        } else if(registrationId.equals("google")) {
            email = (String) response.get("email");
        } else {
            throw new OAuth2AuthenticationException("허용되지 않은 인증입니다.");
        }

        User user;
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent()) {
            user = optionalUser.get();
        }else{
            user = new User();
            user.setEmail(email);
            user.setRole(Role.ROLE_USER);
            userRepository.save(user);
        }

        httpSession.setAttribute("user", user);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString()))
                , oAuth2User.getAttributes()
                , userNameAttributeName
        );
    }
}
