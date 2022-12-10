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
    <title>글쓰기</title>
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
</head>
<body>

<form id="form" method="post">
    제목 : <input type="text" name="title" id="title">
    내용
<%--    : <input type="text" name="content" id="content">--%>
    <textarea name="editor" id="editor"></textarea>

    <input type="submit" id="insertButton" value="제출">
</form>
<script>
    ClassicEditor
        .create( document.querySelector( '#editor' ), {language : "ko"} )
        .catch( error => {
            console.error( error );
        } );

    let form = document.querySelector("#form");
    form.addEventListener('submit', (event) => {
        event.preventDefault();
        let content = document.getElementById("editor").value;

        let param = {
            "title" : document.getElementById("title").value,
            "content" : content,
            "type" : "qna"
        }
        event.preventDefault();
        fetch('${pageContext.request.contextPath}/board/notice/insert', {
            method : 'POST',
            headers : {
                'Content-Type' : 'application/json;charset=utf-8'
            },
            body : JSON.stringify(param)
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
