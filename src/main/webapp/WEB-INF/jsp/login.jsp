<%--
  Created by IntelliJ IDEA.
  User: ejy1024
  Date: 2022/12/04
  Time: 5:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>로그인</title>
</head>
<body>
  ${errorMsg}
  <form action="${pageContext.request.contextPath}/login" name="frmLogin" method="post">
    아이디 <label><input type="text" name="user_id" autofocus></label><br/>

    비밀번호 <label><input type="password" name="user_pw"></label><br/>
    <input type="submit" value="로그인">
    <input type="reset" value="다시입력"> <br/>
    <a href="/board/normal/list?type=normal&pageIndex=1&pageSize=10">일반 게시판</a> <br/>
    <a href="register.jsp">회원가입</a> <br/>
    <a href="search.jsp">아이디 및 비밀번호 찾기</a>
  </form>
</body>
</html>
