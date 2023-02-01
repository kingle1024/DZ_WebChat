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
    <script src="https://code.jquery.com/jquery-2.2.4.min.js"
            integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=" crossorigin="anonymous"></script>
    <script>
        window.load = function () {

        }

    </script>
</head>
<body>
<a href="${pageContext.request.contextPath}/board/notice/view?no=${board.bno}">이전</a>

<form id="editForm" method="post" enctype="multipart/form-data">
    제목 : <input type="text" name="title" id="title" value="${board.btitle}"> <br/>
    내용
    <textarea name="editor" id="editor">${board.bcontent}</textarea>
    <input type="hidden" id="bno" name="bno" value="${board.bno}">
    <table>
        <c:forEach var="file" items="${board.boardFiles}">
            <tr>
                <td>
                    <a href="${pageContext.request.contextPath}/boardFile/download?filename=${file.realName}">${file.orgName}</a>
                </td>
            </tr>
        </c:forEach>
    </table>
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
    <input type="submit" id="edit" name="edit" value="수정">
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
<script>

    let editor;
    ClassicEditor
        .create(document.querySelector('#editor'), {language: "ko"})
        .then(newEditor => {
            editor = newEditor;
        })
        .catch(error => {
            console.error(error);
        });

    $("#editForm").on('submit', (event) => {
        event.preventDefault();
        fetch('${pageContext.request.contextPath}/board/insert', {
            method: 'POST',
            cache: 'no-cache',
            body: new FormData(form)
        })
            .then(response => response.json())
            .then(jsonResult => {
                alert(jsonResult.message);
                if (jsonResult.status === true)
                    location.href = jsonResult.url;
            });
    });

</script>
</body>
</html>
