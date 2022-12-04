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
  <form action="${pageContext.request.contextPath}/login" name="frmLogin" method="post" encType="UTF-8">
    아이디 <input type="text" name="user_id"><br/>
    비밀번호 <input type="password" name="user_pw"><br/>
    <input type="submit" value="로그인">
    <input type="reset" value="다시입력">
    <a href="register.html">회원가입</a>
  </form>
</body>
</html>
