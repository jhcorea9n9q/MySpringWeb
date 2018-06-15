<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>

<head>
	<title>글내용</title>
	<meta charset="UTF-8">
	<script type="text/javascript" src="webjars/jquery/3.3.1/dist/jquery.min.js"></script>
	<script>
		$(document).ready(function(){

				$.ajax({
					type: "post",
					url: "bbld"
				}).done(function(data) {
					var d = JSON.parse(data);
					var list = d.list;
					$("#list").empty();
					for(var i=0; i<list.length; i++) {
						var html = "<li>";
						
						/********************************************/
							html += "<a href='bSelect?boardNo=";
							html += list[i].boardNo;
							html += "'>";
							html += list[i].boardTitle;
							html += "</a>";
						/********************************************/
							
							html += "</li>";
						$("#list").append(html);
					}
				});

		});
	</script>
</head>

<body>
	<h1>리스트!</h1>
	<a href="bInsert">글 작성하기</a>
	<ul id="list"></ul>
</body>
</html>
