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
    <jsp:include page="/WEB-INF/jsp/template/js.jsp" />
</head>

<body>
<form id="insertForm" method="post" enctype="multipart/form-data">
    제목 : <input type="text" name="title" id="title">
    내용
    <textarea name="editor" id="editor"></textarea>
    <table>
        <tbody>
        <tr>
            <th><label>첨부파일</label></th>
            <td><input type="file" name="filename1"></td>
            <td><input type="button" value="추가" class="insertFile"></td>
        </tr>
        </tbody>
        <tfoot>
        <tr style="display:none">
            <th><label>첨부파일</label></th>
            <td><input type="file" name="filename1"></td>
            <td><input type="button" value="추가" class="insertFile"></td>
            <td><input type="button" value="삭제" class="deleteFile"></td>
        </tr>
        </tfoot>
    </table>
    <input type="hidden" id="type" name="type" value="qna">
    <input type="submit" id="insertButton" value="제출">
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
        fetch('${pageContext.request.contextPath}/board/insert', {
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
