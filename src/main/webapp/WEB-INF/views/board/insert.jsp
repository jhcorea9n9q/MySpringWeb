<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>

<head>
	<title>글작성!</title>
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
		#file {
			border: none;
		}
		#textin {
			height: 200px;
		}
	</style>
	<script type="text/javascript" src="webjars/jquery/3.3.1/dist/jquery.min.js"></script>
	<script>
		$(document).ready(function(){
			
			$("form").submit(function(e){
				e.preventDefault();
				
				$.ajax({
					type: "post",
					url: "http:\/\/FileServer\/FileUpload\/Lee_JH",
					data: new FormData($(this)[0]),
					contentType: false,
					cache: false,
					processData: false
				}).done(function(data) {
					var d = JSON.parse(data);
					console.log(d);
// 					d.upload는 파라미터로 보내기엔 Type이 맞지 않아 String 변환이 필요하다.
					
					$.ajax({
						type: "post",
						url: "bid",
						data: {
							"boardTitle":$("form input").eq(0).val(),
							"boardContents":$("form input").eq(1).val(),
							"data":JSON.stringify(d.upload) // 스트링 타입으로 변환.
						}
					}).done(function(data) {
						var d = JSON.parse(data);
						alert(d.msg);
						if(d.status == 1) {
							location.href="bSelect?boardNo=" + d.boardNo;	
						}
					});
					
				});
				
				
				
			});
			
		});
	</script>
</head>

<body>
	<h1>글작성!</h1>
	<form enctype="multipart/form-data">
		<input type="text" name="boardTitle" placeholder="제목을 입력해 주십시오."><br><br>
		<input type="text" name="boardContents" id="textin" placeholder="내용을 입력해 주십시오."><br><br>
		<input type="file" name="file" id="file" multiple="multiple"><br><br>
		<input type="submit" id="submit_button" value="글 작성하기"><br><br><br>
		<a href="bList">리스트로 돌아가기</a>
	</form>
</body>
</html>
