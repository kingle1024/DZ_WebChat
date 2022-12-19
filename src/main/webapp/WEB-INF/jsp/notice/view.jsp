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
    <script>
        window.onload = function (){
            const myState = '${myStatus}';
            if(myState == 'like'){
                document.querySelector("#like").style.backgroundColor = '#008CBA';
            }else if(myState == 'dislike') {
                document.querySelector("#dislike").style.backgroundColor = '#f44336';
            }
        }
    </script>
    <style>
        table{

        }
    </style>
</head>
<body>
<c:if test="${board.bwriterId eq login_id}">
    <a href="${pageContext.request.contextPath}/board/edit?bno=${board.bno}">수정</a><br/>
    <button id="del">삭제</button><br/>
</c:if>

<table>
    <tr>
        <td>글 번호</td><td>${board.bno}</td>
    </tr>
    <tr>
        <td>제목</td><td>${board.btitle}</td>
    </tr>
    <tr>
        <td>작성일</td><td>${board.bdate}</td>
    </tr>
    <tr>
        <td>내용</td><td>${board.bcontent}</td>
    </tr>
</table>
<table>
<c:forEach var="file" items="${boardFiles}" >
    <tr>
        <td><a href="/boardFile/download?filename=${file.realName}">${file.orgName}</a></td>
    </tr>
</c:forEach>
</table>

<button id="like">좋아유 <span id="likeCount">${like}</span></button>
<button id="dislike">싫어유 <span id="dislikeCount">${dislike}</span></button>
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
                        location.href = jsonResult.url;
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
        likeButton.style.backgroundColor = '#008CBA'; // blue
    }

    let dislikeButton = document.querySelector("#dislike");
    dislikeButton.onclick = () => {
        let btype = "dislike";
        let params = {
            "bno" : ${board.bno},
            "type" : btype
        }
        boardPopularity(params, btype);
        dislikeButton.style.backgroundColor = '#f44336'; // red
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

                if(jsonResult.status == "add"){
                    if(btype == "like"){
                        count = document.getElementById("likeCount");
                        count.textContent = parseInt(count.textContent) +1;
                    }else{
                        count = document.getElementById("dislikeCount");
                        count.textContent = parseInt(count.textContent) +1;
                    }
                } else if(jsonResult.status == "cancel"){
                    if(btype == "like"){
                        count = document.getElementById("likeCount");
                        count.textContent = parseInt(count.textContent) -1;
                        count.style.backgroundColor = "";
                        document.getElementById("like").style.backgroundColor='';
                    }else{
                        count = document.getElementById("dislikeCount");
                        count.textContent = parseInt(count.textContent) -1;
                        count.style.backgroundColor = "";
                        document.getElementById("dislike").style.backgroundColor='';
                    }
                } else if(jsonResult.status == "change"){
                    if(btype == "like"){
                        let likeCnt = document.getElementById("likeCount");
                        let dislikeCnt = document.getElementById("dislikeCount");

                        likeCnt.textContent = (parseInt(likeCnt.textContent) +1);
                        dislikeCnt.textContent = (parseInt(dislikeCnt.textContent) -1);
                        document.getElementById("dislike").style.backgroundColor='';
                    }else{
                        let likeCnt = document.getElementById("likeCount");
                        let dislikeCnt = document.getElementById("dislikeCount");

                        likeCnt.textContent = (parseInt(likeCnt.textContent) -1);
                        dislikeCnt.textContent = (parseInt(dislikeCnt.textContent) +1);
                        document.getElementById("like").style.backgroundColor='';
                    }
                }


            });
    }

</script>
</body>
</html>