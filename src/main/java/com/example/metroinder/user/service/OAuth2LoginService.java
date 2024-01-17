package com.example.metroinder.user.service;


import com.example.metroinder.user.model.UserAccount;
import com.example.metroinder.user.repository.UserAccountRepository;
import com.example.metroinder.user.role.UserRole;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class OAuth2LoginService {
    /*@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String client_id;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String secret_id;*/

    private final UserAccountRepository userAccountRepository;
    public String getSnsAccessToken(String code/*, String provider*/) {

        String snsAccessToken = "";
        String requestUrl = "";
        try {
            /*if("google".equals(provider)){
            requestUrl = "";
            return null;
        }
        else if("naver".equals(provider)){
            requestUrl = "";
            return null;
        }
        else{*/
            requestUrl = "https://kauth.kakao.com/oauth/token";
            RestTemplate rt = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");


            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", "34c757199de774ac66043a9ce8baef95");
            params.add("redirect_uri", "http://localhost:8080/auth/kakao/callback");
            params.add("client_secret", "6nesdmQCr1DaSGvJDOW7rMl6wQR4KVaG");
            params.add("code", code);


            HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                    new HttpEntity<>(params, headers);


            ResponseEntity<String> response =
                    rt.exchange(requestUrl, HttpMethod.POST, kakaoTokenRequest, String.class);

            log.info(response.getHeaders().toString());

            String responseBody = response.getBody();

            StringBuilder stringBuilder = new StringBuilder();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(responseBody);
            snsAccessToken = jsonObject.get("access_token").toString();

            //log.info(snsAccessToken);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return snsAccessToken;
    }

    public Map<String, String> getUserInfo(/*String provider,*/ String accessToken) {
        String requestUrl = "";
        Map<String, String> userInfo = new HashMap<>();
        try {
            /*if("google".equals(provider)){
            requestUrl = "";
            return null;
        }
        else if("naver".equals(provider)){
            requestUrl = "";
            return null;
        }
        else{*/
                requestUrl = "https://kapi.kakao.com/v2/user/me";
                RestTemplate rt = new RestTemplate();

                //HttpHeader 오브젝트
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "Bearer " + accessToken);
                headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

                //http 헤더(headers)를 가진 엔티티
                HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                        new HttpEntity<>(headers);

                //reqUrl로 Http 요청 , POST 방식
                ResponseEntity<String> response =
                        rt.exchange(requestUrl, HttpMethod.POST, kakaoProfileRequest, String.class);

                String responseBody = response.getBody();

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(responseBody);
                //String providerId = provider + "_" + jsonObject.get("id").toString();
                String providerId = "kakao_" + jsonObject.get("id").toString();
                String connectedAt = jsonObject.get("connected_at").toString();
                JSONObject account = (JSONObject) jsonObject.get("kakao_account");
                String userName = ((JSONObject) account.get("profile")).get("nickname").toString();
                String email = account.get("email").toString();

                userInfo.put("providerId", providerId);
                userInfo.put("connectedAt", connectedAt);
                userInfo.put("userName", userName);
                userInfo.put("email", email);
            //}
        } catch (Exception e){
            e.printStackTrace();
        }
        return userInfo;
    }

    public UserAccount saveUser(String provider, Map<String, String> userInfo) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse(userInfo.get("connectedAt"), inputFormatter);

        String providerId = userInfo.get("providerId");
        String connectedAt = date.format(outputFormatter);
        String userName = userInfo.get("userName");
        String email = userInfo.get("email");
        UserRole role = UserRole.ROLE_USER;

        UserAccount findUser = userAccountRepository.findByProviderAndEmail(provider, email);

        if(findUser == null) {

            findUser = UserAccount.builder()
                    .username(userName).email(email).role(role)
                    .provider(provider).providerId(providerId)
                    .connectedAt(connectedAt)
                    .build();

            userAccountRepository.save(findUser);
        }

        return findUser;
    }


    public boolean updateRefreshToken(UserAccount userAccount, String refreshToken) {
        if(userAccount != null) {
            userAccount.updateRefreshToken(refreshToken);
            userAccountRepository.save(userAccount);
            return true;
        }else {
            log.info("일치하는 회원이 없습니다.");
            return false;
        }
    }
}
