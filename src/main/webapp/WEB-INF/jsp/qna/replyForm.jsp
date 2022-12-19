<%--
  Created by IntelliJ IDEA.
  User: ejy1024
  Date: 2022/12/06
  Time: 8:57 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>답변 글쓰기</title>
    <style>
        /* 넓이 높이 조절 */
        .ck.ck-editor {
            max-width: 1000px;
        }
        .ck-editor__editable {
            min-height: 300px;
        }

    </style>
    <script src="https://cdn.ckeditor.com/ckeditor5/35.3.2/classic/ckeditor.js"></script>
    <script src="https://cdn.ckeditor.com/ckeditor5/35.3.2/classic/translations/ko.js"></script>
    <script src="https://code.jquery.com/jquery-2.2.4.min.js" integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=" crossorigin="anonymous"></script>
</head>
<body>
<h1>답변글</h1>
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

<form id="insertForm" method="post" enctype="multipart/form-data">
    제목 : <input type="text" name="title" id="title">
    내용<br/>
    <textarea name="editor" id="editor"></textarea>
    <input type="submit" id="insertButton" value="제출">
    <input type="hidden" id="type" name="type" value="qna">
    <input type="hidden" id="bno" name="bno" value="${board.bno}">
</form>
<script type="text/javascript">
    let tbody = $("tbody")[0];
    let tr = $("tfoot tr")[0];
    let insertFile = $(".insertFile");

    function insertFileEventHandler() {
        let newTr = tr.cloneNode(true);
        tbody.append(newTr);
        newTr.style.display = "";

        newTr.querySelector(".insertFile").addEventListener("click", insertFileEventHandler);
        newTr.querySelector(".deleteFile").addEventListener("click", e => {
            tbody.removeChild(e.target.parentNode.parentNode)
        });
    }

    insertFile.on("click", insertFileEventHandler);
</script>

<script type="text/javascript">
    ClassicEditor
        .create(document.querySelector( '#editor' ), {language : "ko"} )
        .catch( error => {
            console.error( error );
        } );

    $("#insertForm").on('submit', (event) => {
        event.preventDefault();
        fetch('${pageContext.request.contextPath}/board/replyForm', {
            method : 'POST',
            cache: 'no-cache',
            body : new FormData($('#insertForm')[0])
        })
            .then(response => response.json())
            .then(jsonResult => {
                alert(jsonResult.message);
                if(jsonResult.status === true)
                    location.href = jsonResult.url;
            });
    });
</script>
</body>
</html>
