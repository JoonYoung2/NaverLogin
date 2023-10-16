<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	
	<h2>게이트 페이지</h2>
    <div>
    <c:choose>
        <c:when test="${USER_INFO eq null}">
            <a href="/naverLogin">로그인 페이지</a>
        </c:when>
        <c:otherwise>
            <c:if test="${USER_INFO.loginType eq 'naver'}">
                <h3>네이버 아이디로 접속중</h3>
                <span><a href="remove?token=${USER_INFO.accessToken }">로그아웃</a></span>
<!--                 <span><a href="/logout">로그아웃</a></span> -->
                <div>
                    <div>
                        <span>
                            <image src="${USER_INFO.profileImage}"/>
                        </span>
                        <span>${USER_INFO.id}</span>
                    </div>
                    <div>${USER_INFO.name}</div>
                    <div>${USER_INFO.profileImage}</div>
                    <div>${USER_INFO.age}</div>
                    <div>${USER_INFO.gender}</div>
                    <div>${USER_INFO.loginType}</div>
                    <br>
                    ${USER_INFO.accessToken }<br><br>
                    ${USER_INFO.resultStr }               
                </div>
            </c:if>
        </c:otherwise>
    </c:choose>
    </div>
</body>
</html>