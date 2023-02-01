<%--
  Created by IntelliJ IDEA.
  User: ejy1024
  Date: 2022/12/11
  Time: 4:08 PM
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
    제목 : <input type="text" name="title" id="title"> <br/>
    등록자 : <input type="text" name="bwriter" id="bwriter"> <br/>
    패스워드 : <input type="password" name="password" id="password"> <br/>
    패스워드 확인 <input type="password" name="rePassword" id="rePassword"> <br/>
    내용 <br/>
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
        var password = document.getElementById("password").value;
        var rePassword = document.getElementById("rePassword").value;

        if(password != rePassword){
            alert("패스워드가 서로 다릅니다.");
            return;
        }

        let content = document.getElementById("editor").value;

        let param = {
            "title" : document.getElementById("title").value,
            "content" : content,
            "type" : "normal",
            "writer" : document.getElementById("bwriter").value,
            "password" : password,
            "rePassword" : rePassword
        }
        event.preventDefault();
        fetch('${pageContext.request.contextPath}/board/normal/insert', {
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