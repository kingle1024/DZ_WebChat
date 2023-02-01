<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>글쓰기 작성</title>
  <style type="text/css">

    .ck.ck-editor {
      max-width: 1000px;
    }
    .ck-editor__editable {
      min-height: 300px;
    }
  </style>
  <script src="https://cdn.ckeditor.com/ckeditor5/35.3.2/classic/ckeditor.js"></script>
  <script src="https://cdn.ckeditor.com/ckeditor5/35.3.2/classic/translations/ko.js"></script>
  <script
          src="https://code.jquery.com/jquery-2.2.4.min.js"
          integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44="
          crossorigin="anonymous"></script>
</head>
<script type="text/javascript">

  $(document).ready(() => {
    //alert(boardForm.innerHTML);
    //alert($(boardForm).html());
    //alert($("#boardForm").html());
  });

</script>
<body>
<form name="boardForm" id="boardForm" method="post" enctype="multipart/form-data">
  <label>제목</label>
  <input type="text" name="title" id="title" ><br/>
  <label>내용</label>
  <input type="text" name="content" id="content"/><br/>

  <table>
    <tbody>
    <tr>
      <th><label>첨부파일</label></th>
      <td><input type="file" name="filename1" id="filename1"/></td>
      <td><input type="button" value="추가" class="insertFile"></td>
    </tr>
    </tbody>
    <tfoot>
    <tr style="display:none">
      <th><label>첨부파일</label></th>
      <td><input type="file" name="filename1" id="filename1"/></td>
      <td><input type="button" value="추가" class="insertFile"></td>
      <td><input type="button" value="삭제" class="deleteFile"></td>
    </tr>
    </tfoot>
  </table>
  <br/>
  <input type="submit" id="write" name="write" value="등록"  >
  <input type="button" name="cancle" value="취소" onClick="history.back()">
</form>
<script type="text/javascript">
  let tbody = document.querySelector("tbody");
  let tr = document.querySelector("tfoot tr");
  let insertFile = document.querySelector(".insertFile");

  function insertFileEventHandler() {
    let newTr = tr.cloneNode(true);
    tbody.append(newTr);
    newTr.style.display = "";

    newTr.querySelector(".insertFile").addEventListener("click", insertFileEventHandler);
    newTr.querySelector(".deleteFile").addEventListener("click", e => {
      tbody.removeChild(e.target.parentNode.parentNode)
    });
  }

  insertFile.addEventListener("click", insertFileEventHandler);


  let boardForm = document.querySelector("#boardForm");
  boardForm.addEventListener("submit", (e) => {
    e.preventDefault();

    fetch('/pro/board/write', {
      method : 'POST',
      cache: 'no-cache',
      body: new FormData(boardForm)
    })
            .then(response => response.json())
            .then(jsonResult => {
              alert(jsonResult.message);
              if (jsonResult.status == true) {
                location.href = jsonResult.url;
              }
            });
  });

</script>
</body>
</html>