package com.example.metroinder.user.model;

import com.example.metroinder.dataSet.model.Timestamped;
import com.example.metroinder.user.role.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;


@Entity
@NoArgsConstructor
@Getter
@Setter
public class UserAccount extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 200)
    private String username;
    @Column(length = 50)
    private String email;
    @Enumerated(EnumType.STRING)
    @Setter
    @Column(length = 10)
    private UserRole role;
    @Column(length = 10)
    private String provider;    // oauth2를 이용할 경우 어떤 플랫폼을 이용하는지
    //@Column(length = 30)
    private String providerId;  // oauth2를 이용할 경우 아이디값
    @Column(length = 300)
    private String refreshToken; // 리프레시 토큰
    @Column(length = 30)
    private String connectedAt;

    @Builder
    public UserAccount(String username, String email, UserRole role, String provider, String providerId, String connectedAt, String refreshToken) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.connectedAt = connectedAt;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
