package Chat;

import org.json.JSONObject;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// handshake 설정하기 위한 클래스를 지정한다.
@ServerEndpoint(value = "/broadsocket/{roomName}", configurator = WebSocketSessionConfigurator.class)
public class BroadSocket {
//    private Map<Session, EndpointConfig> configs = Collections.synchronizedMap(new HashMap<>());
    private static Map<String, HashSet<String>> roomAndUserSet = new ConcurrentHashMap<>();
    private static final Map<String, Session> userAndSocketSession = new HashMap<>();
    private static Map<String, Integer> roomAndParticipantsCount = new HashMap<>();
    private EndpointConfig endpointConfig;
    // handshake가 끝나면 handleOpen이 호출된다.
    @OnOpen
    public void handleOpen(Session socketSession, EndpointConfig config,
                           @PathParam("roomName") String roomName) {
        endpointConfig = config;
        if(isListPage(socketSession)){
            sendToClient_RoomAndParticipants(socketSession);
            return;
        }

        String userId = getUserId(config);
        // 페이지 새로고침 시 웹소켓 세션 갱신
        refreshUserSocketSession(socketSession, userId);

        // 방에 입장
        int participantsCount =
                roomAndParticipantsCount.getOrDefault(roomName, 0);
        roomAndParticipantsCount.put(roomName, participantsCount + 1);
        HashSet<String> userList = roomAndUserSet.getOrDefault(roomName, new HashSet<>());

        // 방이 있으면 해당 방에 본인이 있는지 확인
        if(userList.size() < 1 || !userList.contains(userId)){
            userList.add(userId);
            System.out.println("userList:"+userList);
        }

        // 갱신된 정보를 다시 맵에 넣음
        roomAndUserSet.put(roomName, userList);
    }

    // 클라이언트로 부터 메시지가 오면 handleMessage가 호출 된다.
    @OnMessage
    public void handleMessage(String message, Session userSession, @PathParam("roomName") String roomName) {
        System.out.println("onMessage:"+roomName);
        JSONObject json = new JSONObject(message);
        String msg = (String) json.get("message");
        String sender = (String) json.get("sender");

        HashSet<String> userList = roomAndUserSet.get(roomName);
        message = "[상대방] " + sender + " => " + msg;

        if(msg.startsWith("#w")) { // #w 123 q
            sendWhisper(userSession, msg, sender);
        }else{
            sendToClient(message, userSession, userList);
        }
    }

    private static void sendWhisper(Session userSession, String msg, String sender) {
        try {
            String[] msgSplit = msg.split(" ");

            if(msgSplit.length < 3){
                userSession.getBasicRemote().sendText("[나에게만] 귓속말 형식이 다릅니다! #w {id} {message} ");
                return;
            }

            String targetId = msgSplit[1];
            String targetMsg = msgSplit[2];
            Session userSocketSession = userAndSocketSession.get(targetId);
            if(userSocketSession == null){
                userSession.getBasicRemote().sendText("[나에게만] 귓속말 상대방 아이디가 존재하지 않습니다! " + targetId);
                return;
            }

            String message = "[귓속말] " + sender + " => " + targetMsg;
            userSocketSession.getBasicRemote().sendText(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @OnClose
    public void handleClose(Session socketSession, @PathParam("roomName") String roomName) {
        System.out.println("client is now disconnected...");

        System.out.println("now roomAndParticipantsCount : "+ roomAndParticipantsCount);
        // index 페이지에서도 새로고침하면 close가 발생한다.
        // 채팅 브라우저 새로고침하면 -1, +1
        // 채팅 브라우저를 닫으면 -1
        if(isListPage(socketSession)){

        }else{
            if(existParticipants(roomName)) {
                int participantsCount = roomAndParticipantsCount.get(roomName);
                roomAndParticipantsCount.put(roomName, participantsCount -1);
                HttpSession session = (HttpSession) endpointConfig.getUserProperties().get(WebSocketSessionConfigurator.Session);
                String userId = (String) session.getAttribute("login_id");
                userAndSocketSession.remove(userId);
                roomAndUserSet.remove(userId);
            }else
                roomAndParticipantsCount.remove(roomName);
        }
    }

    @OnError
    public void handleError(Throwable e, Session userSession) {
        e.printStackTrace();
    }
    private static void refreshUserSocketSession(Session socketSession, String userId) {
        userAndSocketSession.put(userId, socketSession);
    }

    private static void sendToClient(String message, Session userSession, HashSet<String> userList) {
        Iterator<String> it = userList.iterator();
        while(it.hasNext()){
            String userName = it.next();
            Session userSocketSession = userAndSocketSession.get(userName);
            if(userSocketSession.equals(userSession)) continue;

            try {
                userSocketSession.getBasicRemote().sendText(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void sendToClient_RoomAndParticipants(Session socketSession) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("roomAndParticipants", roomAndParticipantsCount);

        try {
            socketSession.getBasicRemote().sendText(String.valueOf(jsonObject));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getUserId (EndpointConfig config) {
        HttpSession session = (HttpSession) config.getUserProperties().get(WebSocketSessionConfigurator.Session);
        String userId = (String) session.getAttribute("login_id");
        return userId;
    }

    private static boolean isListPage(Session socketSession) {
        return "list".equals(socketSession.getQueryString());
    }

    private static boolean existParticipants(String roomName) {
        return roomAndParticipantsCount != null
                && roomAndParticipantsCount.get(roomName) > 0;
    }
}
