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
    <a href="/board/notice/edit?bno=${board.bno}">수정</a><br/>
</c:if>

${board.bno} <br/>
${board.btitle} <br/>
${board.bcontent} <br/>
${board.bdate}
</body>
</html>
