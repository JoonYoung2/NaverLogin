package com.login.project.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.login.project.dto.NaverLoginDTO;
import com.login.project.service.NaverLoginService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class NaverLoginController {
	private final NaverLoginService service;
	
	@GetMapping("/")
	public String home() {
		return "index";
	}
	
	@GetMapping("/naverLogin")
	public String login(Model model, HttpSession session) {
		String url = service.getAuthorizationUrl(session);
		model.addAttribute("naverAuthUrl", url);
		
		return "naver_login";
	}
	
	@RequestMapping(value = "/logout")
    public String Logout(HttpSession session) {
        if(session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }
	
	@GetMapping("/oauth2/login")
    public String Oauth2Login(@RequestParam String code, @RequestParam String state, HttpSession session, Model model) throws IOException, JsonParseException {
        OAuth2AccessToken oauthToken;
        oauthToken = service.getAccessToken(session, code, state);
        String[] imsi = oauthToken.toString().substring(31).split(",");
        String accessToken = imsi[0];
        /*네이버 프로필 정보 가져오기*/
        String result = service.getUserProfile(oauthToken);

        Object obj = null;

        JsonParser parser = new JsonParser();

        try {
            obj = parser.parse(result);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        JsonObject jobj = (JsonObject) obj;
        JsonObject res_obj = (JsonObject) jobj.get("response");

        NaverLoginDTO user = new NaverLoginDTO();

        user.setId( res_obj.get("email").toString());
        user.setName(res_obj.get("nickname").toString());
        user.setProfileImage(res_obj.get("profile_image").toString());
        user.setAge(res_obj.get("age").toString());
        user.setGender(res_obj.get("gender").toString());
        user.setLoginType("naver");
        String access_token = oauthToken.getAccessToken();
        String str_result = access_token.replaceAll("\\\"","");
        user.setAccessToken(str_result);

        session.setAttribute("USER_INFO", user);
        

        return "redirect:/";
    }	
	
	@GetMapping("/remove") //token = access_token임
	public String remove(@RequestParam String token,HttpSession session, HttpServletRequest request, Model model ) {
		log.info("토큰 삭제중...");
		String apiUrl = "https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id="+service.CLIENT_ID+
		"&client_secret="+service.CLIENT_SECRET+"&access_token="+token+"&service_provider=NAVER";
		
			try {
				String res = requestToServer(apiUrl);
				model.addAttribute("res", res); //결과값 찍어주는용
			    session.invalidate();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		    return "redirect:/";
	}
	
	private String requestToServer(String apiURL) throws IOException {
	    return requestToServer(apiURL, null);
	  }
	
	 private String requestToServer(String apiURL, String headerStr) throws IOException {
		    URL url = new URL(apiURL);
		    HttpURLConnection con = (HttpURLConnection)url.openConnection();
		    con.setRequestMethod("GET");
		    System.out.println("header Str: " + headerStr);
		    if(headerStr != null && !headerStr.equals("") ) {
		      con.setRequestProperty("Authorization", headerStr);
		    }
		    int responseCode = con.getResponseCode();
		    BufferedReader br;
		    System.out.println("responseCode="+responseCode);
		    if(responseCode == 200) { // 정상 호출
		      br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		    } else {  // 에러 발생
		      br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		    }
		    String inputLine;
		    StringBuffer res = new StringBuffer();
		    while ((inputLine = br.readLine()) != null) {
		      res.append(inputLine);
		    }
		    br.close();
		    if(responseCode==200) {
		    	String new_res=res.toString().replaceAll("&#39;", "");
				 return new_res; 
		    } else {
		      return null;
		    }
		  }
	
}
