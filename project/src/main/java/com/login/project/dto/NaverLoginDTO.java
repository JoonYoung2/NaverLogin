package com.login.project.dto;

import lombok.Data;

@Data
public class NaverLoginDTO {
	private String id;

    private String email;

    private String nickname;

    private String profileImage;

    private String age;

    private String gender;

    private String name;

    private String resultCode;

    private String resultStr;

    private String accessToken;

    private String refreshToken;

    private String tokenType;

    private String expiresIn;

    private String loginType;
}
