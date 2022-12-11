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
</head>
<body>
<form action="">
    <label for="search"></label><input type="text" placeholder="검색할 제목 입력" id="search" name="search">
    <input type="submit" value="검색">
</form>
  제목 : <input type="text" name="btitle" id="btitle" value="${board.btitle}"> <br/>
  내용 <textarea name="editor" id="editor"></textarea> <br/>
  <button id="edit">수정</button>
<script>
    let editor;
    ClassicEditor
        .create( document.querySelector( '#editor' ), {language : "ko"} )
        .then (newEditor => {
            editor = newEditor;
        })
        .catch( error => {
            console.error( error );
        } );

  let editButton = document.getElementById("edit");
  editButton.onclick = () => {
      let param = {
          "btitle" : document.getElementById("btitle").value,
          "bcontent" : editor.getData(),
          "bno" : ${board.bno}
      }

      fetch('/board/notice/edit', {
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
