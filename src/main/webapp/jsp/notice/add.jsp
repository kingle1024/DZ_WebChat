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
    <script type="text/javascript" data-cfasync="false" charset="utf-8" src="${pageContext.request.contextPath}/resources/smarteditor2/js/HuskyEZCreator.js" charset="utf-8"></script>
    <script type="text/javascript" src="//code.jquery.com/jquery-1.11.0.min.js"></script>

    <script>
        $(document).ready(function () {
            <!-- SmartEditor2 텍스트편집기 -->
            var oEditors = [];
            function smartEditorIFrame() {
                nhn.husky.EZCreator.createInIFrame({
                    oAppRef: oEditors,
                    elPlaceHolder: "bo_content",
                    sSkinURI: "/resources/smarteditor2/SmartEditor2Skin.html",
                    fCreator: "createSEditor2"
                });
            }
            smartEditorIFrame();
        });
    </script>

</head>
<body>

<form id="form" method="post">
    제목 : <input type="text" name="title" id="title">
    내용 : <input type="text" name="content" id="content">
<%--    <textarea class="form-control" rows="20" name="bo_content" id="bo_content"></textarea>--%>

    <input type="submit" id="insertButton" value="제출">
</form>
<script>

    let form = document.querySelector("#form");
    form.addEventListener('submit', (event) => {
        event.preventDefault();
        let content = oEditors.getById["bo_content"].exec("UPDATE_CONTENTS_FIELD",[]);

        let param = {
            "title" : document.getElementById("title").value,
            "content" : content
        }
        event.preventDefault();
        console.log('hi');
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
