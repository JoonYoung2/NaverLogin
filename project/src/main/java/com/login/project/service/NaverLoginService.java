package com.login.project.service;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.login.project.repository.NaverLoginRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NaverLoginService {
	
	private final NaverLoginRepository repo;
	public final static String CLIENT_ID = "TrviwHjmQ7spnClM4qEh";
    public final static String CLIENT_SECRET = "duVJlH03Ok";
    public final static String SESSION_STATE = "state";
    public final static String REDIRECT_URI = "http://localhost:8080/oauth2/login";
    public final static String REDIRECT_LOGUT_URI = "http://localhost:8080/oauth2/logout";
    public final static String PROFILE_API_URL = "https://openapi.naver.com/v1/nid/me";
	
    public String getUserProfile(OAuth2AccessToken oauthToken) throws IOException {
        OAuth20Service oauthService = new ServiceBuilder()
                .apiKey(CLIENT_ID)
                .apiSecret(CLIENT_SECRET)
                .callback(REDIRECT_URI)
                .build(NaverLoginApi.instance());

        OAuthRequest request = new OAuthRequest(Verb.GET, PROFILE_API_URL, oauthService);
        oauthService.signRequest(oauthToken, request);
        Response response = request.send();

        return response.getBody();
    }
    
	public String getAuthorizationUrl(HttpSession session) {
		String state = generateRandomString();
        /*생성한 난수를 세션에 저장*/
        setSession(session, state);
        /* Scribe에서 제공하는 인증 URL 생성 기능을 이용하여 네이버 아이디로 인증 URL 생성 */
//        OAuth20Service oAuthService = new ServiceBuilder()
//                .apiKey(CLIENT_ID)
//                .apiSecret(CLIENT_SECRET)
//                .callback(REDIRECT_URI)
//                .state(state)
//                .build(NaverLoginApi.instance());
//        
//        System.out.println("naver URL = " + oAuthService.getAuthorizationUrl());
//        return oAuthService.getAuthorizationUrl();
        String url = "https://nid.naver.com/oauth2.0/authorize?response_type=code";
        url += "&client_id=" + CLIENT_ID;
        url += "&redirect_uri=" + REDIRECT_URI;
        url += "&state=" + state;
        return url;
        
	}
	
	 /*네이버 로그인 AccessToken 획득*/
    public OAuth2AccessToken getAccessToken(HttpSession session, String code, String state) throws IOException {
        String sessionState = getSession(session);

        if (StringUtils.pathEquals(sessionState, state)) {
            OAuth20Service oAuthService = new ServiceBuilder()
                    .apiKey(CLIENT_ID)
                    .apiSecret(CLIENT_SECRET)
                    .callback(REDIRECT_URI)
                    .state(state)
                    .build(NaverLoginApi.instance());

            OAuth2AccessToken accessToken = oAuthService.getAccessToken(code);

            return accessToken;
        }

        return null;
    }
    
    /*네이버 로그인 AccessToken 획득*/
    public OAuth2AccessToken getLogoutAccessToken(HttpSession session, String code, String state) throws IOException {
        String sessionState = getSession(session);

        if (StringUtils.pathEquals(sessionState, state)) {
            OAuth20Service oAuthService = new ServiceBuilder()
                    .apiKey(CLIENT_ID)
                    .apiSecret(CLIENT_SECRET)
                    .callback(REDIRECT_URI)
                    .state(state)
                    .build(NaverLoginApi.instance());

            OAuth2AccessToken accessToken = oAuthService.getAccessToken(code);

            return accessToken;
        }

        return null;
    }
    
    private String getSession(HttpSession session) {
        return (String) session.getAttribute(SESSION_STATE);
    }
	
	private String generateRandomString() {
        return UUID.randomUUID().toString();
    }
	
	private void setSession(HttpSession session, String state) {
        session.setAttribute(SESSION_STATE, state);
    }
}
