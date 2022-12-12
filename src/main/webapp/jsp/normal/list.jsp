<%--
  Created by IntelliJ IDEA.
  User: ejy1024
  Date: 2022/12/11
  Time: 3:39 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>일반 게시판</title>
</head>
<body>
<h1>일반 게시판</h1>

<c:choose>
    <c:when test="${isLogon}">
        <a href="/login">메인 페이지</a> <br/>
    </c:when>
    <c:otherwise>
        <a href="/jsp/login.jsp">로그인</a> <br/>
    </c:otherwise>
</c:choose>

<a href="/jsp/normal/add.jsp">글쓰기</a> <br/>
<form name="searchForm" id="searchForm" action="" method="get">
    <label for="search"></label><input type="text" placeholder="검색할 제목 입력" id="search" name="search" autofocus>
    <input type="submit" value="검색">
</form>
<table>
    <thead>
    <tr>
        <td>no</td>
        <td>제목</td>
        <td>작성자</td>
        <td>등록일</td>
        <td>조회수</td>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="board" items="${boardsList}" varStatus = "status">
        <tr>
            <td>${status.count}</td>
            <td><a href="${pageContext.request.contextPath}/board/normal/view?no=${board.bno}">${board.btitle}</a></td>
            <td>${board.bwriter}</td>
            <td>${board.bdate}</td>
            <td>${board.bhit}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div>${pager}</div>
</body>
</html>
