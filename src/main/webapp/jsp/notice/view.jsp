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
    <a href="${pageContext.request.contextPath}/board/notice/edit?bno=${board.bno}">수정</a><br/>
    <button id="del">삭제</button><br/>
</c:if>

${board.bno} <br/>
${board.btitle} <br/>
${board.bcontent} <br/>
${board.bdate}
<button id="like">좋아유 <span id="likeCount">0</span></button>
<button id="dislike">싫어유 <span id="dislikeCount">0</span></button>
<script>
    let delButton = document.querySelector("#del");
    let bno = ${board.bno};
    if(delButton != null) {
        delButton.onclick = () => {
            fetch('/board/del?bno=' + bno)
                .then(response => response.json())
                .then(jsonResult => {
                    alert(jsonResult.message);
                    if (jsonResult.status === true) {
                        // location.href = jsonResult.url;
                        // TODO location으로 변경해야 함.
                        history.back();
                    }
                });
        };
    }
    let likeButton = document.querySelector("#like");
    likeButton.onclick = () => {
        let btype = "like";
        let params = {
            "bno" : ${board.bno},
            "type" : btype
        }
        boardPopularity(params, btype);
    }

    let dislikeButton = document.querySelector("#dislike");
    dislikeButton.onclick = () => {
        let btype = "dislike";
        let params = {
            "bno" : ${board.bno},
            "type" : btype
        }
        boardPopularity(params, btype);
    }

    function boardPopularity(params, btype){
        fetch('${pageContext.request.contextPath}/board/popularity', {
            method : 'POST',
            headers : {
                'Content-Type' : 'application/json;charset=utf-8'
            },
            body : JSON.stringify(params)
        })
            .then(response => response.json())
            .then(jsonResult => {
                alert(jsonResult.message);
                let count;
                if(btype == "like"){
                    count = document.getElementById("likeCount");
                }else{
                    count = document.getElementById("dislikeCount");
                }
                count.textContent = parseInt(count.textContent) +1;
            });
    }

</script>
</body>
</html>