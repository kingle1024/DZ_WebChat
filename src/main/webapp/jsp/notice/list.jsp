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
공지사항
<c:if test="${true eq login_admin}">
    <a href="/jsp/notice/add.jsp">글쓰기</a>
</c:if>
<a href="${pageContext.request.contextPath}/board/notice">공지사항</a>
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
            <td><a href="${pageContext.request.contextPath}/board/notice/view?no=${board.bno}">${board.btitle}</a></td>
            <td>${board.bwriter}</td>
            <td>${board.bdate}</td>
            <td>${board.bhit}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
