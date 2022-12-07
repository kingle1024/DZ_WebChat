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

<form method="post" id="editForm">
    <input type="submit" id="editSubmit" value="수정"> <br/>
    user id : <span id="userIdSpan">${member.userId}</span> <br/>
    user name  : <span><input type="text" name="name" id="name" value="${member.name}"></span> <br/>
    user email : <input type="text" name="email" id="email" value="${member.email}"> <br/>
    user phone : <input type="text" name="phone" id="phone" value="${member.phone}"> <br/>
    user registerDate <span>${member.createdate}</span><br/>
    user loginDate : <span>${member.loginDateTime} </span><br/>
</form>
<br/><a href="#" id="withdraw">탈퇴</a>

<a href="logout">로그아웃</a><br/><br/>
<a href="${pageContext.request.contextPath}/">main</a>
<script>
    <%--let form = document.querySelector("#editForm");--%>
    <%--form.addEventListener('submit', (event) => {--%>
    <%--    event.preventDefault();--%>
    <%--    console.log(this.name.value);--%>
    <%--    let param = {--%>
    <%--        "name" : this.name.value,--%>
    <%--        "email" : this.email.value,--%>
    <%--        "phone" : this.phone.value--%>
    <%--    }--%>
    <%--    console.log(param.name);--%>

    <%--    fetch('${pageContext.request.contextPath}/member/edit', {--%>
    <%--        method : 'POST',--%>
    <%--        headers : {--%>
    <%--            'Content-Type' : 'application/json;charset=utf-8'--%>
    <%--        },--%>
    <%--        body : JSON.stringify(param)--%>
    <%--    })--%>
    <%--        .then(response => response.json())--%>
    <%--        .then(jsonResult => {--%>
    <%--            alert(jsonResult);--%>
    <%--            if(jsonResult.status === false){--%>
    <%--                alert(jsonResult.message);--%>
    <%--            }else{--%>
    <%--                // location.href = jsonResult.url;--%>
    <%--            }--%>
    <%--        });--%>
    <%--});--%>

    let editButton = document.querySelector("#editSubmit");

    editButton.onclick = (event) => {
        event.preventDefault();
        edit(event);
    };

    function edit(event){
        let param = {
            "name" : document.getElementById("name").value,
            "email" : document.getElementById("email").value,
            "phone" : document.getElementById("phone").value
        }

        fetch('${pageContext.request.contextPath}/member/edit', {
            method : 'POST',
            headers : {
                'Content-Type' : 'application/json;charset=utf-8'
            },
            body : JSON.stringify(param)
        })
            .then(response => response.json())
            .then(jsonResult => {
                alert(jsonResult.message);
                if(jsonResult.status){
                    location.href = jsonResult.url;
                }
            });
    }

    let withdrawButton = document.querySelector("#withdraw");
    withdrawButton.onclick = (event) => {
        event.preventDefault();

        fetch('${pageContext.request.contextPath}/member/withdraw')
            .then(response => response.json())
            .then(jsonResult => {
                alert(jsonResult.message);
                location.href = jsonResult.url;
            });
    }
</script>
</body>
</html>
