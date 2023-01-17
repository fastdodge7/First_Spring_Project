package com.fastdodgespring.config.auth;

import com.fastdodgespring.config.auth.dto.OAuthAttributes;
import com.fastdodgespring.config.auth.dto.SessionUser;
import com.fastdodgespring.domain.user.User;
import com.fastdodgespring.domain.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;


@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException
    {
        System.out.println("LoadUser called!");
        OAuth2UserService<OAuth2UserRequest, OAuth2User>
                delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 현재 로그인을 어디에서 하고 있는지 구분하는 코드
        // 구글로 진행하는지, 네이버로 하는지 등을 구별하는 코드임.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // userNameAttributeName, 당최 무슨 역할을 하는 필드인지 이해하기 어렵다.
        // 이 필드는 소셜 서비스(구글, 네이버 등)마다, 로그인 할 때 유저를 구분하는
        // Primary key로 사용되는 필드의 이름이 달라서, 그 필드 이름을 나타내는 필드이다.
        // 구글에서는 sub라는 필드에 해당 키가 저장되고, 네이버에서는 id라는 필드에 해당 키가 저장된다.
        // 따라서, 구글에서는 userNameAttributeName이 "sub"가 되고, 네이버에서는 "id"가 된다.
        // 이 값을 get 메서드에 집어넣어서 실질적인 key를 가져올 수 있다.
        // OAuth를 지원하는 소셜서비스마다 유저를 구분하는 key의 필드명이 달라서, 이를 해결하기 위해 만든 듯 하다.
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();


        // OAuthAttributes : 일종의 DTO. OAuth2UserService를 통해 가져온 OAuth2User의 필드들을 담는다.
        OAuthAttributes attributes = OAuthAttributes
                .of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

        // SessionUser : 세션에 유저 정보를 저장하기 위한 DTO 클래스임.
        // User 클래스를 쓰지 않고 따로 DTO 클래스를 만드는 이유가 있음.
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User( // 얜 뭘까
                Collections.singleton(
                        new SimpleGrantedAuthority(user.getRoleKey())), // 얜 또 뭘까
                        attributes.getAttributes(),
                        attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes)
    {
        // 구글 사용자 정보가 업데이트 될 수도 있어서 업데이트 기능을 추가시켜 놓은 상태.
        // loadUser로 소셜 서비스로부터 유저 정보를 받아오고, saveOrUpdate로 갱신 or 추가.
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());
        System.out.println("Save Or Update called!");
        return userRepository.save(user);
    }
}
