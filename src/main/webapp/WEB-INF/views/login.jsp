<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<title>LOGIN</title>
	<meta charset="UTF-8">
	<style>
		body {
			text-align: center;
		}
		input {
			border-radius: 5px;
			border: 1px solid lightgrey;
			padding: 5px 10px;
			width: 300px;
		}
		#submit_button {
			border-color: black;
			width: auto;
		}
	</style>
</head>
<body>
	<h1>로그인</h1>
	<form action="userSelect" method="post">
		<input type="email"    name="userEmail"    placeholder="사용할 이메일을 입력해 주세요."  maxlength="100" required="required"><br><br>
		<input type="password" name="userPassword" placeholder="사용할 비밀번호를 입력해 주세요." maxlength="10" required="required"><br><br>
		<input id="submit_button" type="submit" value="로그인하기">
	</form>
</body>
</html>
