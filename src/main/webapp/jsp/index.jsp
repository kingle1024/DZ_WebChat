<%--
  Created by IntelliJ IDEA.
  User: ejy1024
  Date: 2022/12/04
  Time: 3:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>메인</title>
</head>
<body>
회원님의 아이디는.. ${login_id} <br/>
${login_name} <br/>

<a href="${pageContext.request.contextPath}/member/view">내정보 보기</a>
<c:if test="${true eq login_admin}">
    <a href="${pageContext.request.contextPath}/admin/memberList">회원 리스트 보기</a>
</c:if>
<a href="${pageContext.request.contextPath}/board/notice">공지사항</a>
<a href="${pageContext.request.contextPath}/board/qna">QnA게시판</a>
<a href="${pageContext.request.contextPath}/chat/list?list">채팅</a>
<a href="logout">로그아웃</a> <br/>
</body>
</html>
