<%--
  Created by IntelliJ IDEA.
  User: ejy1024
  Date: 2022/12/05
  Time: 2:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>회원가입</title>
    <script src="https://code.jquery.com/jquery-2.2.4.min.js" integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=" crossorigin="anonymous"></script>
</head>
<body>
<form action="/member/insert" method="post" id="signUpForm">
    <div class="info-enter">
        <div class="inner">
            <h3>정보입력</h3>
            <div class="info-cont">
                <ul>
                    <li>
                        <input type="text" id="userName" name="userName" pattern=".{2,12}"
                               title="3 char" value=""
                               onfocus="if(this.value === '이름') this.value = '';" placeholder="이름">
                    </li>
                    <li>
                        <input type="text" id="id" minlength="2" name="id" placeholder="아이디">
                        <div id="uid_valid_msg"></div>
                    </li>
                    <li>
                        <input type="password" id="pwd" name="pwd" placeholder="비밀번호">
                        <a href="#" onclick="OpenPasswordGuide();"
                           onfocus="FocusIn_PwdForm(this);"
                           title="새창으로 열림">비밀번호 도움말</a>
                        <!-- 비밀번호가 안전한 경우 input에 class="usable" 추가, 비밀번호가 조건에 맞지않는 경우 class="incorrect" 추가 -->
<%--                        <p class="enter-guide">안전한 사용을 위해 영문, 숫자, 특수문자 조합 8~15자를 사용해 주세요.</p>--%>
                        <p class="txt01" style="display:none">완벽한 비밀번호 입니다.</p>
                        <!-- 비밀번호가 안전한 경우 -->
                        <p class="txt02" style="display:none" id="password_err">유추하기 쉬운 비밀번호 입니다.</p>
                        <!-- 비밀번호가 조건에 맞지않는 경우 -->
                    </li>
                    <li>
                        <input type="password" id="rePassword" name="rePassword" placeholder="비밀번호 재입력">
                    </li>
                    <li>
                        <select id="mobile_tel1">
                            <option value="">휴대폰 번호 선택</option>
                            <option value="010">010</option>
                            <option value="011" selected>011</option>
                        </select>
                        -
                        <input type="text" name="mobile_tel2" id="mobile_tel2" value="">
                        -
                        <input type="text" name="mobile_tel3" id="mobile_tel3" value="">
                    </li>
                    <li>
                        <input type="text" name="email" id="email" value="" placeholder="이메일">
                        @
                        <select id="emailDomain">
                            <option value="">선택</option>
                            <option value="naver.com" selected>naver.com</option>
                            <option value="daum.net">daum.net</option>
                        </select>
                    </li>
                    <li>
                        <select id="sd">
                        <c:forEach var="sd" items="${sds}" varStatus = "status">
                            <option value="${sd}">${sd}</option>
                        </c:forEach>
                        </select>
                        -
                        <select id="sgg"></select>
                        -
                        <select id="sdName"></select>
                    </li>
                </ul>
            </div>
        </div>
    </div>
<%--    <div class="terms-agree">--%>
<%--        <div class="inner">--%>
<%--            <h3>약관동의</h3>--%>
<%--            <div class="terms-cont">--%>
<%--                <div class="all-agree">--%>
<%--                    <label for="cb_agreeAll">--%>
<%--                        <input type="checkbox" id="cb_agreeAll" onclick="ToggleAllProvisions(this);">--%>
<%--                        전체동의--%>
<%--                    </label>--%>
<%--                </div>--%>
<%--                <div class="terms-count-inner">--%>
<%--                    <div class="check-set">--%>
<%--                        <label for="cbProvision1">--%>
<%--                            <input type="checkbox" onclick="ConfirmProvision(this)"--%>
<%--                                   id="cbProvision1" name="cbProvision1">--%>
<%--                            옥션 이용약관--%>
<%--                            <span class="text_importance"> (필수)</span>--%>
<%--                        </label>--%>
<%--                        <a target="_blank" class="txt-view-all" href="#">전체보기</a>--%>
<%--                    </div>--%>

<%--                    <div class="check-set">--%>
<%--                        <label for="cbFinance">--%>
<%--                            <input type="checkbox" onclick="ConfirmProvision(this);"--%>
<%--                                   id="cbFinance" name="cbFinance">--%>
<%--                            전자금융거래 이용약관--%>
<%--                            <span class="text_importance"> (필수)</span>--%>
<%--                        </label>--%>
<%--                        <a target="_blank" class="txt-view-all" href="#">전체보기</a>--%>
<%--                    </div>--%>

<%--                    <div class="check-set">--%>
<%--                        <label for="cbThirdParty">--%>
<%--                            <input type="checkbox" onclick="ConfirmProvision(this);"--%>
<%--                                   id="cbThirdParty" name="cbThirdParty">--%>
<%--                            개인정보 제3자(판매자) 제공--%>
<%--                            <span class="text_importance"> (선택)</span>--%>
<%--                        </label>--%>
<%--                        <a href="#" class="txt-view-all" onclick="window.open('https://memberssl.auction.co.kr/membership/signup/popup/Popup_ThirdParty.aspx', 'CLIENT_WINDOW', 'resizable=yes scrollbars=yes')">내용보기</a>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--    <input type="submit" id="signUpButton">--%>
    <input type="button" value="회원가입" id="insertButton">
    <input type="button" id="back" value="이전">
</form>

    <script>
        let id = document.querySelector("#id");
        id.onblur = () => { // blur는 포커스가 다른 곳으로 이동했을 때를 말함.
            dupUidCheck();
        }

        let insertButton = document.querySelector("#insertButton");

        insertButton.onclick = (event) => {
            if (valid(event)) {
                event.preventDefault();
                signUp();
            }
        };

    </script>
        <script>
            function valid(event) {
                let password = document.querySelector("#pwd").value;
                let rePassword = document.querySelector("#rePassword").value;
                if(password !== rePassword){
                    alert("비밀번호가 서로 다릅니다.");
                    return false;
                }

                return true;
            }

            function signUp(){
                // 회원가입 처리
                fetch('/member/dupUidCheck?id='+id.value)
                    .then(response => response.json())
                    .then(jsonResult => {
                        let uid_valid_msg = document.querySelector("#uid_valid_msg");
                        uid_valid_msg.innerHTML = "[" + id.value + "]" +jsonResult.message;
                        if(jsonResult.status === false){
                            alert("[" + id.value + "]" + jsonResult.message);
                        }else{
                            let mobile_tel = document.querySelector("#mobile_tel1");
                            let emailDomain = document.querySelector("#emailDomain");
                            mobile_tel = mobile_tel.options[mobile_tel.selectedIndex].value;
                            emailDomain = emailDomain.options[emailDomain.selectedIndex].value

                            let param = {
                                "id" : id.value,
                                "pwd" : pwd.value,
                                "email" : this.email.value + "@" +emailDomain,
                                "userName" : this.userName.value,
                                "phone" : mobile_tel + "-" + this.mobile_tel2.value+"-"+this.mobile_tel3.value
                            };

                            fetch('/member/insert', {
                                method : 'POST',
                                headers : {
                                    'Content-Type' : 'application/json;charset=utf-8'
                                },
                                body: JSON.stringify(param)
                            })
                                .then(response => response.json())
                                .then(jsonResult => {
                                    alert(jsonResult.message);
                                    if(jsonResult.status === true) {
                                        location.href = jsonResult.url;
                                    }
                                });
                        }
                    });
            }
        </script>
<script>
    async function dupUidCheck(){
        let response = await fetch('/member/dupUidCheck?id='+id.value);
        let jsonResult = await response.json();
        let uid_valid_msg = document.querySelector("#uid_valid_msg");
        uid_valid_msg.innerHTML = "[" + id.value + "]" + jsonResult.message;
    }
</script>
<script>
    $("#sd").change( () => {
        let selectVal = $("#sd").val();
        async function getSgg(){
            let response = await fetch('/access/sgg?sgg='+selectVal);
            let jsonResult = await response.json();
            console.log(jsonResult.sgg);
            let html = "";
            let sgg = jsonResult.sgg;
            for(let key in sgg){
                html += "<option>"+sgg[key]+"</option>";
            }
            $("#sgg").html(html);
        }
        getSgg();
    });
    $("#sgg").change( () => {
        let selectVal = $("#sgg").val();
        async function getSdName(){
            let response = await fetch('/access/sdName?sdName='+selectVal);
            let jsonResult = await response.json();
            let html = "";
            let sdName = jsonResult.sdName;
            for(let key in sdName){
                html += "<option>"+sdName[key]+"</option>";
            }
            $("#sdName").html(html);
        }
        getSdName();
    })

</script>
<script>
    const calcForm = document.querySelector("#signUpForm");
    calcForm.addEventListener("submit", (event) => {

        // 숫자가 있는 케이스
        let re = /([0-9]+)/g;

        if(won.value === ""){
            alert("원화를 입력해주세요.");
            won.focus();
            event.preventDefault();
            return false;
        }

        // 숫자가 1개라도 있는가?
        if(!re.test(won.value)){
            alert("수치를 입력해주세요.");
            won.focus();
            event.preventDefault();
            return false;
        }

        alert('가입되었습니다.');

        return true;
    });
</script>
<script>
    function OpenPasswordGuide() {
        window.open("https://memberssl.auction.co.kr/Membership/IDPW/popup_passhelp.html",
            "PasswordGuide",
            "toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=yes,copyhistory=0"
        );
    }

    function FocusIn_PwdForm(obj){
        if (obj.value === '') {
            obj.value = '';
        }
    }

    function ToggleAllProvisions(obj){
        // const checkbox =
    }

    function ConfirmProvision(obj){

    }
</script>
</body>
</html>
