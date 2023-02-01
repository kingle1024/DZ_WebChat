<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ejy1024
  Date: 2022/12/06
  Time: 7:23 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>공지사항</title>
    <script src="https://code.jquery.com/jquery-2.2.4.min.js" integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=" crossorigin="anonymous"></script>
</head>
<body>

<h1>공지사항</h1>
<a href="${pageContext.request.contextPath}/login">메인</a>
<c:if test="${true eq login_admin}">
    <a href="${pageContext.request.contextPath}/board/notice/add">글쓰기</a>
</c:if>
<form name="searchForm" id="searchForm" action="list" method="get">
    <label for="search"></label><input type="text" placeholder="검색할 제목 입력" id="search" name="search" autofocus>
    <input type="hidden" id="type" name="type" value="${type}">
    <input type="hidden" id="pageIndex" name="pageIndex" value="${param.pageIndex}">
    <input type="hidden" id="pageSize" name="pageSize" value="${param.pageSize}">
    <input type="submit" value="검색" value="${param.search}">
</form>
<table>
    <thead>
        <tr>
            <td>no</td>
            <td>제목</td>
            <td>작성자</td>
            <td>등록일</td>
            <td>조회수</td>
        </tr>
    </thead>
    <tbody id="tbody">
    <c:forEach var="board" items="${page.list}" varStatus = "status">
        <tr>
            <td>${board.bno}</td>
            <td>
                <a href="${pageContext.request.contextPath}/board/notice/view?no=${board.bno}">
                    ${board.bno == board.parentNo ? '' : '<span style="margin-left:20px;">[답변]</span>'} ${board.btitle}
                </a>
            </td>
            <td>${board.bwriter}</td>
            <td>${board.bdate}</td>
            <td>${board.bhit}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div id="pager">${pager}</div><br/>
<script>
    function searchF(index){
        let start = new Date();
        let search = document.getElementById("search").value;
        let type = document.getElementById("type").value;
        let pageIndex = document.getElementById("pageIndex").value;
        if(index !== undefined){
            pageIndex = index;
        }
        let pageSize = document.getElementById("pageSize").value;

        fetch('${pageContext.request.contextPath}/board/search?search='+search+'&type='+type+'&pageIndex='+pageIndex+'&pageSize='+pageSize)
            .then(response => response.json())
            .then(jsonResult => {
                $("#tbody").empty();
                let html = "";
                let board = jsonResult.boardsList;
                for(let key in board){
                    let isReply = board[key].bno === board[key].parentNo ? "" : "<span style='margin-left:20px;'>[답변]</span>";
                    html += "<tr>";
                    html += "<td>" + board[key].bno + "</td>";
                    html += "<td><a href='${pageContext.request.contextPath}/board/notice/view?no="+board[key].bno+"'>" +
                        isReply + board[key].btitle +
                        "</a></td>";
                    html += "<td>" + board[key].bwriter + "</td>";
                    html += "<td>" + board[key].bdate + "</td>";
                    html += "<td>" + board[key].bhit + "</td>";
                    html += "</tr>";
                }
                document.querySelector("#tbody").innerHTML = html;

                html = jsonResult.pager;

                $("#pager").empty();
                document.querySelector("#pager").innerHTML = html;
                let end = new Date();
                console.log("실행기간 : ");
                console.log(end-start);
            });
    }
</script>
<script>
    $("#searchForm").on('submit', (event) => {
        event.preventDefault();
        searchF();
    });
</script>
</body>
</html>
