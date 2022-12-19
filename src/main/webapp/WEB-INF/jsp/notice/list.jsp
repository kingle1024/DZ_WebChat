<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ejy1024
  Date: 2022/12/06
  Time: 7:23 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>공지사항</title>
</head>
<body>
<h1>공지사항</h1>
<c:if test="${true eq login_admin}">
    <a href="/WEB-INF/jsp/notice/add.jsp">글쓰기</a>
</c:if>
<form name="searchForm" id="searchForm" action="list" method="get">
    <label for="search"></label><input type="text" placeholder="검색할 제목 입력" id="search" name="search" autofocus>
    <input type="hidden" id="type" name="type" value="${type}">
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
            <td>${board.bno}</td>
            <td>
                <a href="${pageContext.request.contextPath}/board/notice/view?no=${board.bno}">
                    ${board.bno == board.parentNo ? '' : '<span style="margin-left:20px;">[답변]</span>'} ${board.btitle}
                </a>
            </td>
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
