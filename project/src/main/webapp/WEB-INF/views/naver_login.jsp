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
${naverAuthUrl }<br>
	<a href="${naverAuthUrl }">네이버 로그인</a><br>
	<a href="naverLogin.do">그냥 실행</a><br>
	${aaa }
</body>
</html>