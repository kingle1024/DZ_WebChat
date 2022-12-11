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

<a href="${pageContext.request.contextPath}/board/notice/edit?bno=${board.bno}">수정</a><br/>
<button id="del">삭제</button><br/>

${board.bno} <br/>
${board.btitle} <br/>
${board.bcontent} <br/>
${board.bdate}
<script>
    let delButton = document.querySelector("#del");
    let bno = ${board.bno};
    delButton.onclick = (event) => {
        let password = prompt("게시글의 암호를 입력하세요.");
        let param = {
            "bno": bno,
            "password": password
        }

        fetch('/board/normal/del', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(param)
        })
            .then(response => response.json())
            .then(jsonResult => {
                alert(jsonResult.message);
                if (jsonResult.status === true) {
                    location.href = jsonResult.url;
                }
            });
    }



</script>
</body>
</html>