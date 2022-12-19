<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ejy1024
  Date: 2022/12/06
  Time: 8:02 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>글 상세</title>
</head>
<body>
<c:if test="${board.bwriterId eq login_id}">
    <a href="/board/edit?bno=${board.bno}">수정</a><br/>
</c:if>
<c:if test="${board.bno == board.parentNo}">
    <a href="${pageContext.request.contextPath}/board/replyForm?bno=${board.bno}">답글</a><br/>
</c:if>
<table>
    <tr>
        <td>글 번호</td><td>${board.bno}</td>
    </tr>
    <tr>
        <td>제목</td><td>${board.btitle}</td>
    </tr>
    <tr>
        <td>작성일</td><td>${board.bdate}</td>
    </tr>
    <tr>
        <td>내용</td><td>${board.bcontent}</td>
    </tr>
</table>
<table>
    <c:forEach var="file" items="${boardFiles}" >
        <tr>
            <td><a href="/boardFile/download?filename=${file.realName}">${file.orgName}</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
