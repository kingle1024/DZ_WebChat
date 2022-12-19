<%--
  Created by IntelliJ IDEA.
  User: ejy1024
  Date: 2022/12/07
  Time: 7:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <script src="https://code.jquery.com/jquery-2.2.4.min.js" integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=" crossorigin="anonymous"></script>
    <script>
        window.load = function (){

        }

    </script>
</head>
<body>
<form id="editForm" method="post" enctype="multipart/form-data">
  제목 : <input type="text" name="title" id="title" value="${board.btitle}"> <br/>
  내용
  <textarea name="editor" id="editor">${board.bcontent}</textarea>
  <input type="hidden" id="bno" name="bno" value="${board.bno}">
  <table>
      <c:forEach var="file" items="${boardFiles}" >
          <tr>
              <td><a href="/boardFile/download?filename=${file.realName}">${file.orgName}</a></td>
          </tr>
      </c:forEach>
  </table>
    <input type="button" id="addFile" name="addFile">
  <div id="addFileList"></div><br/>
  <input type="submit" id="edit" name="edit" value="수정">
</form>
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

    let editForm = $("#editFormt");
    editForm.addEventListener('submit', (event) => {
        event.preventDefault();
        fetch('${pageContext.request.contextPath}/board/insert', {
            method : 'POST',
            cache: 'no-cache',
            body : new FormData(form)
        })
            .then(response => response.json())
            .then(jsonResult => {
                alert(jsonResult.message);
                if(jsonResult.status === true)
                    location.href = jsonResult.url;
            });
    });

    let addDiv = $("#addFileList");

    document.querySelector("#addFile").onclick = () => {
        addDiv.innerHTML += "<input type='file' name='boardFile'><br/> ";
    }
</script>
</body>
</html>
