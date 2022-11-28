package com.example.metroinder.auth;

import com.example.metroinder.auth.userinfo.OAuth2UserInfo;
import com.example.metroinder.model.UserAccount;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
@ToString
public class PrincipalDetails implements OAuth2User {
    private UserAccount userAccount;
    private OAuth2UserInfo oAuth2UserInfo;

    public PrincipalDetails(UserAccount userAccount, OAuth2UserInfo oAuth2UserInfo) {
        //PrincipalOauth2UserService 참고
        this.userAccount = userAccount;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userAccount.getRole().toString();
            }
        });
        return collect;
    }
/*
    @Override
    public String getPassword() {
        return userAccount.getPassword();
    }


    @Override
    public String getUsername() {
        return userAccount.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

*/

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2UserInfo.getAttributes();
    }

    @Override
    public String getName() {
        return oAuth2UserInfo.getProviderId();
    }
}
