package com.website.user.kakao;

import com.website.user.kakao.dto.KakaoTokenResponse;
import com.website.user.kakao.dto.KakaoUserResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Slf4j
public class Kakao {
    private final String token;
    @Getter
    private final KakaoUserResponse user;
    private final WebClient webClient = WebClient.create();
    private final String redirectUri;
    private final String clientId;
    public Kakao(String code, String redirectUri, String clientId){
        this.redirectUri = redirectUri;
        this.clientId = clientId;
        this.token = this.getKakaoUserToken(code);
        this.user = this.getKakaoUserInfo(this.token);
    }

    private String getKakaoUserToken(String code){
        try {
            KakaoTokenResponse res = webClient.post()
                    .uri("https://kauth.kakao.com/oauth/token")
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                    .bodyValue("grant_type=authorization_code" +
                            "&client_id=" + this.clientId +
                            "&redirect_uri=" + this.redirectUri +
                            "&code=" + code)
                    .retrieve()
                    .bodyToMono(KakaoTokenResponse.class)
                    .block();
            return Objects.requireNonNull(res).getAccessToken();
        } catch (Exception e){
            log.error("Kakao.class에서 카카오 서버 인증 요청 중 에러 발생 내용 : "+e.getMessage()+"\nredirectUri: "+redirectUri+"\nclientId: "+clientId);
            return null;
        }
    }
    private KakaoUserResponse getKakaoUserInfo(String token){
        return webClient.post()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization","Bearer "+token)
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(KakaoUserResponse.class)
                .block();
    }
}
