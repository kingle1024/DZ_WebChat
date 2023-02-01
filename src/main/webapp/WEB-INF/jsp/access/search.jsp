<%--
  Created by IntelliJ IDEA.
  User: ejy1024
  Date: 2022/12/07
  Time: 5:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>아이디/비밀번호 찾기</title>
</head>
<body>
아이디 찾기 <br/>
  name : <input type="text" name="searchId_name" id="searchId_name"> <br/>
  email : <input type="text" name="searchId_email" id="searchId_email"> <br/>
  <input type="button" id="searchId" name="searchId" value="search">

<br/>
<br/>
비밀번호 찾기 <br/>

  id : <input type="text" name="searchPwd_id" id="searchPwd_id"> <br/>
  phone : <input type="text" name="searchPwd_phone" id="searchPwd_phone"> <br/>
  <input type="button" id="searchPwd" name="searchPwd" value="search">


<script>
  let idButton = document.querySelector("#searchId");
  idButton.onclick = (event) => {
    event.preventDefault();
    let params = {
      "name" : document.getElementById("searchId_name").value,
      "email" : document.getElementById("searchId_email").value
    }
    fetch('${pageContext.request.contextPath}/member/searchId', {
      method : 'POST',
      headers : {
        'Content-Type' : 'application/json;charset=utf-8'
      },
      body : JSON.stringify(params)
    })
      .then(response => response.json())
      .then(jsonResult => {
        alert(jsonResult.message);
      });
  }

  let pwdButton = document.querySelector("#searchPwd");

  pwdButton.onclick = (event) => {
    event.preventDefault();
    let params = {
      'userid' : document.getElementById("searchPwd_id").value,
      'phone' : document.getElementById("searchPwd_phone").value
    }

    fetch('${pageContext.request.contextPath}/member/searchPwd',{
      method : 'POST',
      headers : {
        'Content-Type' : 'application/json;charset=utf-8'
      },
      body : JSON.stringify(params)
    })
            .then(response => response.json())
            .then(jsonResult => {
              alert(jsonResult.message);
            });
  }
</script>
</body>
</html>
