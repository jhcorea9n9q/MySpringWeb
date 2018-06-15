<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>

<head>
	<title>업데이트!</title>
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
			var delData = [];
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
					update(d);
				});
			});
			
			function update(d){
				$.ajax({
					type: "post",
					url: "bud",
					data: {
						"boardNo" : boardNo,
						"boardTitle" : $("form input").eq(0).val(),
						"boardContents" : $("form input").eq(1).val(),
						"data" : JSON.stringify(d.upload),
						"delData" : JSON.stringify(delData)
					}
				}).done(function(data){
					var d = JSON.parse(data);
					alert(d.msg);
					if(d.status == 1) {
						location.href="bSelect?boardNo=" + d.boardNo;	
					}
				});
			}
			
			
			function boardHTML(data){
				var title = data.boardTitle;
				var contents = data.boardContents;
				$("form input").eq(0).val(title);
				$("form input").eq(1).val(contents);
			}
			
			function filesHTML(data){
				$("#files").empty();
				for (var i=0; i<data.length; i++) {
					var fileURL = data[i].fileURL;
					var html = "<img src='"+fileURL+"' width='70' height='50'>";
					$("#files").append(html);
				}
				
				$("img").on("click",function(){
					var index = $("img").index(this);
					var fileNo = data[index].fileNo;
					console.log(index, fileNo);
					// 선택한 이미지의 정보가 뜨게 할 수 있다.
					
					delData.push({"fileNo":fileNo});
					// 배열 delData에 키밸류 형태로 삭제한 파일을 넣어준다.
					
					data.splice(index, 1);
					$("img").eq(index).remove();
					// 선택한 이미지 삭제
					console.log(data, delData);
				});
			}
			
		});
		
		
		
	</script>
</head>

<body>
	<h1>글수정!</h1>
	<form enctype="multipart/form-data">
		<input type="text" name="boardTitle" placeholder="제목을 입력해 주십시오."><br><br>
		<input type="text" name="boardContents" id="textin" placeholder="내용을 입력해 주십시오."><br><br>
		<input type="file" name="file" id="file" multiple="multiple"><br><br>
		<input type="submit" id="submit_button" value="글 작성하기"><br><br><br>
		<a href="bList">리스트로 돌아가기</a>
	</form>
	<div id="files"></div>
</body>
</html>
