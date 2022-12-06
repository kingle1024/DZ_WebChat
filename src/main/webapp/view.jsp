
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
view.jsp 페이지 <br/>

user id : ${member.userId} <br/>
user name  : ${member.name} <br/>
user email : ${member.email} <br/>
user registerDate : ${member.createdate} <br/>
user loginDate : ${member.loginDateTime} <br/>

<a href="logout">로그아웃</a><br/>
<a href="${pageContext.request.contextPath}/">main</a>
</body>
</html>
