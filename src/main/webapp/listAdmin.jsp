<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ejy1024
  Date: 2022/12/06
  Time: 11:07 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>회원 목록</title>
</head>
<body>
<table>
    <jsp:useBean id="member" scope="request" type="java.util.List"/>
    <c:forEach var="memberBean" items="${member}" varStatus = "listMembersStatus">
        <tr>
            <td>${memberBean.userId}</td>
            <td>${memberBean.name}</td>
            <td>${memberBean.email}</td>

            <td><button onclick="userStatus_noUse('${memberBean.userId}')">미사용</button></td>
            <td><button onclick="userStatus_use('${memberBean.userId}')">사용</button></td>
            <td><button onclick="userStatus_withdraw('${memberBean.userId}')">탈퇴</button></td>
        </tr>
    </c:forEach>
</table>
<script>
    function userStatus_withdraw(id){
        fetch('/admin/member/userStatus_withdraw?id='+id)
            .then(response => response.json())
            .then(jsonResult => {
                alert(jsonResult.message);
            });
    }

    function userStatus_use(id){
        fetch('/admin/member/userStatus_use?id='+id)
            .then(response => response.json())
            .then(jsonResult => {
                alert(jsonResult.message);
            });
    }

    function userStatus_noUse(id){
        fetch('/admin/member/userStatus_noUse?id='+id)
            .then(response => response.json())
            .then(jsonResult => {
                alert(jsonResult.message);
            });
    }
</script>
</body>
</html>
