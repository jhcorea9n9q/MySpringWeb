<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<title>App Server</title>
	<meta charset="UTF-8">
</head>
<body>
	<h1>
		종화 앱 서버.(tomcat1, App_Server)
	</h1>
	<a href="login">로그인</a><br>
	<a href="user">회원가입</a><br><br>
	<c:if test="${JSTLdata.status == 1}">
		${JSTLdata.userName} 님, 로그인을 환영합니다.
	</c:if>
	<c:if test="${JSTLdata.status == 0}">
		지금 입력하신 계정은 존재하지 않습니다.
	</c:if>
	<a href="bList">게시판 목록 보기</a>
</body>
</html>
