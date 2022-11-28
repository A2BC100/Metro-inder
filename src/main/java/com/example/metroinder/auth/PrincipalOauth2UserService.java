package com.example.metroinder.auth;

import com.example.metroinder.auth.userinfo.GoogleUserInfo;
import com.example.metroinder.auth.userinfo.KakaoUserInfo;
import com.example.metroinder.auth.userinfo.NaverUserInfo;
import com.example.metroinder.auth.userinfo.OAuth2UserInfo;
import com.example.metroinder.model.UserAccount;
import com.example.metroinder.model.UserRole;
import com.example.metroinder.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
//DefaultOAuth2UserService는 OAuth2UserService를 구현
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private UserAccountRepository userAccountRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = null;
        String provider = userRequest.getClientRegistration().getRegistrationId(); // 현재 로그인 진행 중인 플랫폼을 구분
        if(provider.equals("google")){
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        else if(provider.equals("naver")){
            oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
        }
        else if(provider.equals("kakao")){
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider+"_"+providerId;
        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String password = bCryptPasswordEncoder.encode("패스워드"+uuid);
        String email = oAuth2UserInfo.getEmail();
        UserRole role = UserRole.ROLE_USER;

        UserAccount findUser = userAccountRepository.findByUsername(username);

        if(findUser == null) {
            findUser = UserAccount.oauth2Register()
                    .username(username).password(password).email(email).role(role)
                    .provider(provider).providerId(providerId)
                    .build();
            userAccountRepository.save(findUser);
        }
        return new PrincipalDetails(findUser, oAuth2UserInfo);
    }
}
