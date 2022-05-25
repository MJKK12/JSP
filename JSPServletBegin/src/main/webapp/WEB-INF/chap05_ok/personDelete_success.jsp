<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원삭제 성공 메시지 출력하기</title>
</head>
<body>

	<h3>${requestScope.delInfo}</h3>
	<br/>
	<button type="button" onclick="javascript:location.href='personSelectAll.do'">회원목록보기</button>
	<%-- 상대경로이기 때문에 personSelectAll.do 앞에 '/' 가 없다. --%>
</body>
</html>