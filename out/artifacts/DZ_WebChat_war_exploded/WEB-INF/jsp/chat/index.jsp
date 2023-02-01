<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head><title>Web Socket Example</title></head>
<body>
<br />
생성할 방 이름 : <input type="text" id="roomName" autofocus>
            <input type="button" id="addRoom" value="방 생성"><br/>
-- 방 목록 --
<div id="roomList"></div>
<script type="text/javascript">
    document.getElementById("addRoom").onclick = () => {
        let roomName = document.getElementById("roomName").value;
        let roomList = document.getElementById("roomList");
        roomList.innerHTML += "<a href='#' onclick='OpenChatRoom(roomName.value)' >"+roomName+"</a> | 입장자 수 0 <br/>";
    };
    // 「WebSocketEx」는 프로젝트 명
    // 「broadsocket」는 호스트 명
    // WebSocket 오브젝트 생성 (자동으로 접속 시작한다. - onopen 함수 호출)
    const webSocket = new WebSocket("ws://localhost:8080/broadsocket/noUseRoomName?list");
    // WebSocket 서버와 접속이 되면 호출되는 함수
    webSocket.onopen = function(message) {
        // 콘솔 텍스트에 메시지를 출력한다.
        // messageTextArea.value = "Server connect...\n";
    };
    // WebSocket 서버와 접속이 끊기면 호출되는 함수
    webSocket.onclose = function(message) {
        // 콘솔 텍스트에 메시지를 출력한다.
        // messageTextArea.value += "Server Disconnect...\n";
    };
    // WebSocket 서버와 통신 중에 에러가 발생하면 요청되는 함수
    webSocket.onerror = function(message) {
        // 콘솔 텍스트에 메시지를 출력한다.
        // messageTextArea.value += "error...\n";
    };
    /// WebSocket 서버로 부터 메시지가 오면 호출되는 함수
    webSocket.onmessage = function(message) {
        // 콘솔 텍스트에 메시지를 출력한다.
        let json = JSON.parse(message.data);
        let roomList = document.getElementById("roomList");

        const map = json.roomAndParticipants;
        let html = "";
        for (const key of Object.keys(map)) {
            const value = map[key];

            html += "<a href='#' onclick=\"OpenChatRoom('" + key + "')\">"+key+"</a> | 입장자 수 : " + value;
            html += "<br/>";
        }
        roomList.innerHTML = html;
    };
    // Send 버튼을 누르면 호출되는 함수
    function sendMessage() {

    }
    // Disconnect 버튼을 누르면 호출되는 함수
    function disconnect() {
        // WebSocket 접속 해제
        webSocket.close();
    }
    function OpenChatRoom(roomName) {
        window.open("/chat/add?p="+roomName,
            "OpenChatRoom",
            "toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=yes,copyhistory=0, width=500, height=820"
        );
    }
</script>
</body>
</html>
