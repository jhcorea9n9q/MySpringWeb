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
			var boardNo = "${param.boardNo}";
			
			if(boardNo == "") {
				alert("누구세요???");
				location.href="/web";
			}else{
				$.ajax({
					type: "post",
					url: "bld",
					data: {"boardNo":boardNo}
				}).done(function(data) {
					var d = JSON.parse(data);
					var boardData = d.boardData;
					var filesData = d.filesData;
					
					boardHTML(boardData);
					filesHTML(filesData);
				});
			}
		});
		
		function boardHTML(data){
			console.log("BOARD : ", data);
			var title = data.boardTitle;
			var contents = data.boardContents;
			$("#title").text(title);
			$("#contents").text(contents);
		}
		
		function filesHTML(data){
			console.log("FILES : ", data);
			for (var i=0; i<data.length; i++) {
				var fileURL = data[i].fileURL;
				var html = "<img src='"+fileURL+"' width='200px'><br>";
				$("#files").append(html);
			}
		}
	</script>
</head>

<body>
<%-- 	<% String boardNo = request.getParameter("boardNo"); %> --%>
<%-- 	<%=boardNo %><br> --%>
<!-- 	위는 Java에서 쓰는 방법이다. JSTL로 하면 밑의 두가지 방식으로 대체 가능 -->
	${param.boardNo}<br>
<%-- 	${paramValues.boardNo[0]}<br> --%>
	<h1 id="title"></h1>
	<p id="contents"></p>
	<div id="files"></div><br><br>
	<div>
		<a href="bList">리스트로 돌아가기</a>
		<a href="bUpdate?boardNo=${param.boardNo}">내용 수정하기</a>
		<a href="#">내용 삭제하기</a>
	</div>
	
</body>
</html>
