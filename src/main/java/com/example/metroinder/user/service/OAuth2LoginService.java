package com.example.metroinder.user.service;


import com.example.metroinder.user.model.UserAccount;
import com.example.metroinder.user.repository.UserAccountRepository;
import com.example.metroinder.user.role.UserRole;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginService {

    @Value("${kakao.client-id}")
    String kakaoClient_id;
    @Value("${kakao.client-secret}")
    String kakaoSecret_id;
    @Value("${kakao.redirect-uri}")
    String kakaoRedirect_uri;
    @Value("${google.client-id}")
    String googleClient_id;
    @Value("${google.client-secret}")
    String googleSecret_id;
    @Value("${google.redirect-uri}")
    String googleRedirect_uri;
    @Value("${naver.client-id}")
    String naverClient_id;
    @Value("${naver.client-secret}")
    String naverSecret_id;
    @Value("${naver.redirect-uri}")
    String naverRedirect_uri;

    private final UserAccountRepository userAccountRepository;

    public String getSnsAccessToken(String code, String provider, String state) {

        String snsAccessToken = "";
        String requestUrl = "";
        try {
            if("google".equals(provider)){
                requestUrl = "https://oauth2.googleapis.com/token";
                RestTemplate rt = new RestTemplate();

                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");


                MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                params.add("grant_type", "authorization_code");
                params.add("client_id", googleClient_id);
                params.add("redirect_uri", googleRedirect_uri);
                params.add("client_secret", googleSecret_id);
                params.add("code", code);


                HttpEntity<MultiValueMap<String, String>> googleTokenRequest =
                        new HttpEntity<>(params, headers);


                ResponseEntity<String> response =
                        rt.exchange(requestUrl, HttpMethod.POST, googleTokenRequest, String.class);

                //log.info("google responseHeader : " + response.getHeaders().toString());
                String responseBody = response.getBody();
                //log.info("google responseBody : " + responseBody);

                StringBuilder stringBuilder = new StringBuilder();
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(responseBody);
                snsAccessToken = jsonObject.get("access_token").toString();

                log.info("google accessToken : " + snsAccessToken);
            }
            else if("naver".equals(provider)){
                requestUrl = "https://nid.naver.com/oauth2.0/token";
                RestTemplate rt = new RestTemplate();

                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");


                MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                params.add("grant_type", "authorization_code");
                params.add("client_id", naverClient_id);
                params.add("redirect_uri", naverRedirect_uri);
                params.add("client_secret", naverSecret_id);
                params.add("code", code);
                params.add("state", state);

                HttpEntity<MultiValueMap<String, String>> naverTokenRequest =
                        new HttpEntity<>(params, headers);


                ResponseEntity<String> response =
                        rt.exchange(requestUrl, HttpMethod.POST, naverTokenRequest, String.class);

                log.info("naver responseHeader : " + response.getHeaders().toString());

                String responseBody = response.getBody();

                log.info("naver responseBody : " + responseBody);
                StringBuilder stringBuilder = new StringBuilder();
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(responseBody);
                snsAccessToken = jsonObject.get("access_token").toString();
            }
            else {
                requestUrl = "https://kauth.kakao.com/oauth/token";
                RestTemplate rt = new RestTemplate();

                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");


                MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                params.add("grant_type", "authorization_code");
                params.add("client_id", kakaoClient_id);
                params.add("redirect_uri", kakaoRedirect_uri);
                params.add("client_secret", kakaoSecret_id);
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
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return snsAccessToken;
    }

    public Map<String, String> getUserInfo(String provider, String accessToken) {
        String requestUrl = "";
        Map<String, String> userInfo = new HashMap<>();
        try {
            if("google".equals(provider)){
                requestUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
                RestTemplate rt = new RestTemplate();

                //HttpHeader 오브젝트
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "Bearer " + accessToken);
                headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

                //http 헤더(headers)를 가진 엔티티
                HttpEntity<MultiValueMap<String, String>> googleProfileRequest =
                        new HttpEntity<>(headers);

                //reqUrl로 Http 요청 , POST 방식
                ResponseEntity<String> response =
                        rt.exchange(requestUrl, HttpMethod.GET, googleProfileRequest, String.class);

                String responseBody = response.getBody();

                log.info("google responseBody : " + responseBody);

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(responseBody);
                String providerId = provider + "_" + jsonObject.get("id").toString();
                String userName = jsonObject.get("name").toString();
                String email = jsonObject.get("email").toString();

                userInfo.put("provider", provider);
                userInfo.put("providerId", providerId);
                userInfo.put("connectedAt", null);
                userInfo.put("userName", userName);
                userInfo.put("email", email);
            }
            else if("naver".equals(provider)){
                requestUrl = "https://openapi.naver.com/v1/nid/me";
                RestTemplate rt = new RestTemplate();

                //HttpHeader 오브젝트
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "Bearer " + accessToken);
                headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

                //http 헤더(headers)를 가진 엔티티
                HttpEntity<MultiValueMap<String, String>> googleProfileRequest =
                        new HttpEntity<>(headers);

                //reqUrl로 Http 요청 , POST 방식
                ResponseEntity<String> response =
                        rt.exchange(requestUrl, HttpMethod.GET, googleProfileRequest, String.class);

                String responseBody = response.getBody();

                log.info("google responseBody : " + responseBody);

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(responseBody);
                JSONObject naverResponse = (JSONObject) jsonObject.get("response");
                String providerId = provider + "_" + naverResponse.get("id").toString();
                String userName = naverResponse.get("nickname").toString();
                String email = naverResponse.get("email").toString();

                userInfo.put("provider", provider);
                userInfo.put("providerId", providerId);
                userInfo.put("connectedAt", null);
                userInfo.put("userName", userName);
                userInfo.put("email", email);
            }
            else{
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

                log.info("KAKAO 프로필 : " + responseBody);

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(responseBody);
                String providerId = provider + "_" + jsonObject.get("id").toString();
                String connectedAt = jsonObject.get("connected_at").toString();
                JSONObject account = (JSONObject) jsonObject.get("kakao_account");
                String userName = ((JSONObject) account.get("profile")).get("nickname").toString();
                String email = account.get("email").toString();

                userInfo.put("provider", provider);
                userInfo.put("providerId", providerId);
                userInfo.put("connectedAt", connectedAt);
                userInfo.put("userName", userName);
                userInfo.put("email", email);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return userInfo;
    }

    public UserAccount saveUser(Map<String, String> userInfo) {
        String connectedAt = userInfo.get("connectedAt");

        if(null != connectedAt) {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime date = LocalDateTime.parse(userInfo.get("connectedAt"), inputFormatter);
            connectedAt = date.format(outputFormatter);
        }

        String provider = userInfo.get("provider");
        String providerId = userInfo.get("providerId");
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
            log.info("save 성공");
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
