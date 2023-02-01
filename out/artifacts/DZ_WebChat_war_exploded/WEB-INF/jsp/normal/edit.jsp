<%--
  Created by IntelliJ IDEA.
  User: ejy1024
  Date: 2022/12/07
  Time: 7:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>수정</title>
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
  등록자 : <input type="text" name="bwriter" id="bwriter" value="${board.bwriter}"> <br/>
  제목 : <input type="text" name="btitle" id="btitle" value="${board.btitle}"> <br/>
  비밀번호 : <input type="text" name="password" id="password" value="${password}"> <br/>

  내용
  <textarea name="editor" id="editor">${board.bcontent}</textarea>

  <button id="edit">수정</button>
<script>
    let editor;
    ClassicEditor
        .create( document.querySelector( '#editor' ), {language : "ko"} )
        .then( newEditor => {
            editor = newEditor;
        } )
        .catch( error => {
            console.error( error );
        } );

  let editButton = document.getElementById("edit");
  editButton.onclick = (event) => {

      let param = {
          "btitle" : document.getElementById("btitle").value,
          "bcontent" : editor.getData(),
          "bno" : ${board.bno},
          "bwriter" : document.getElementById("bwriter").value,
          "password" : document.getElementById("password").value
      }

      fetch('/board/normal/edit', {
          method : 'POST',
          headers : {
              'Content-Type' : 'application/json;charset=utf-8'
          },
          body : JSON.stringify(param)
      })
          .then(response => response.json())
          .then(jsonResult => {
              alert(jsonResult.message);
              if(jsonResult.status === true){
                  location.href = jsonResult.url;
              }
          });
  };
</script>
</body>
</html>
