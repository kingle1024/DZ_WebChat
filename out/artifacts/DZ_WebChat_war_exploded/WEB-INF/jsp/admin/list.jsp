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
    <jsp:include page="/WEB-INF/jsp/template/js.jsp" />
</head>
<body>
${members}
<br/>

========================
<br/>
<a href="${pageContext.request.contextPath}/login">메인</a>
<form name="searchForm" id="searchForm" action="${pageContext.request.contextPath}/admin/member/list" method="get">
    <label for="search"></label><input type="text" placeholder="검색할 이름 입력" id="search" name="search" value="${param.search}" autofocus>
    <input type="hidden" id="pageIndex" name="pageIndex">
    <input type="hidden" id="pageSize" name="pageSize">
    <input type="submit" value="검색">
</form>
<div id="searchSuggest"></div>
<table>
    <thead>
        <td>번호</td>
        <td>아이디</td>
        <td>이름</td>
        <td>이메일</td>
        <td>회원상태</td>
        <td>등록일</td>
        <td>로그인 날짜</td>
        <td colspan="3">상태 변경</td>
    </thead>
    <tbody>
    <jsp:useBean id="member" scope="request" type="java.util.List"/>
    <c:forEach var="memberBean" items="${member}" varStatus = "listMembersStatus">
        <tr>
            <td>${listMembersStatus.count}</td>
            <td>${memberBean.userId}</td>
            <td>${memberBean.name}</td>
            <td>${memberBean.email}</td>
            <td>${memberBean.userStatus}</td>
            <td>${memberBean.createdate}</td>
            <c:choose>
                <c:when test="${not empty memberBean.loginDateTime}">
                    <td>${memberBean.loginDateTime}</td>
                </c:when>
                <c:otherwise>
                    <td>-</td>
                </c:otherwise>
            </c:choose>
            <td><button onclick="userStatus_noUse(this, '${memberBean.userId}')">미사용</button></td>
            <td><button onclick="userStatus_use('${memberBean.userId}')">사용</button></td>
            <td><button onclick="userStatus_withdraw('${memberBean.userId}')">탈퇴</button></td>
            <td>
                <a href="#" class="adminUids" data-uid="${memberBean.userId}" data-admin="${memberBean.admin}">
                    <span>${memberBean.admin == true ? '일반인로 변경' : '관리자로 변경'}</span>
                </a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div>${pager}</div>
<script>
    function userStatus_withdraw(aObj, id){
        fetch('/admin/member/userStatus_withdraw?id='+id)
            .then(response => response.json())
            .then(jsonResult => {
                alert(jsonResult.message);
            });
    }

    function userStatus_use(aObj, id){
        fetch('/admin/member/userStatus_use?id='+id)
            .then(response => response.json())
            .then(jsonResult => {
                alert(jsonResult.message);
            });
    }

    function userStatus_noUse(aObj, id){
        fetch('/admin/member/userStatus_noUse?id='+id)
            .then(response => response.json())
            .then(jsonResult => {
                alert(jsonResult.message);
            });
    }
</script>
<script>
    $(".adminUids").on("click", e => {
        e.preventDefault();
        let link = e.target.parentNode;
        if(!confirm("변경하시겠습니까?")) return;

        let param = {
            'uid' : link.getAttribute("data-uid"),
            'admin' : link.getAttribute("data-admin")
        }
        console.log(param);
    });
</script>
<script>
    // $("#search").addEventListener("keydown", (e) => {
    //     console.log(e);
    //
    //
    // });
    // window.addEventListener("keydown", (e) => console.log(e));
    window.addEventListener("keyup", (e) => {
        // console.log(e.key);
        let query = $("#search").val();
        if(query.length > 0){
            fetch('${pageContext.request.contextPath}/admin/search?query='+query)
                .then(response => response.json())
                .then(jsonResult => {
                    if(jsonResult.status === true){
                        let html = "";
                        for(let key in jsonResult.member){
                            html += jsonResult.member[key].userId +" | ";
                            html += jsonResult.member[key].name;
                            html += "<br/>";
                        }
                        document.querySelector("#searchSuggest").innerHTML = html;
                    }
                });
        }
    });

</script>
</body>
</html>
