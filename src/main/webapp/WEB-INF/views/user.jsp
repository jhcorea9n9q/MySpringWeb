<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<title>USER</title>
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
	<h1>회원가입</h1>
	<form action="userInsert" method="post">
		<input type="email"    name="userEmail"    placeholder="사용할 이메일을 입력해 주세요."  maxlength="100" required="required"><br><br>
		<input type="password" name="userPassword" placeholder="사용할 비밀번호를 입력해 주세요." maxlength="10" required="required"><br><br>
		<input type="text"     name="userName"     placeholder="자신의 이름을 입력해 주세요."    maxlength="50" required="required"><br><br>
		<input id="submit_button" type="submit" value="지금 바로 가입!">
	</form>
</body>
</html>
