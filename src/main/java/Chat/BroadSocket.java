package Chat;

import org.json.JSONObject;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// handshake 설정하기 위한 클래스를 지정한다.
@ServerEndpoint(value = "/broadsocket/{roomName}", configurator = WebSocketSessionConfigurator.class)
public class BroadSocket {
    private Map<Session, EndpointConfig> configs = Collections.synchronizedMap(new HashMap<>());
    private static Map<String, List<String>> roomAndUserList = new ConcurrentHashMap<>();
    private static Map<String, HashSet<String>> roomAndUserSet = new ConcurrentHashMap<>();
    private static final Map<String, Session> userAndSession = new HashMap<>();
    private static HashMap<String, Integer> roomAndParticipantsCount = new HashMap<>();
    // handshake가 끝나면 handleOpen이 호출된다.
    @OnOpen
    public void handleOpen(Session socketSession, EndpointConfig config,
                           @PathParam("roomName") String roomName) {

        if("list".equals(socketSession.getQueryString())){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("roomAndParticipants", roomAndParticipantsCount);

            try {
                socketSession.getBasicRemote().sendText(String.valueOf(jsonObject));
                return;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        HttpSession session = (HttpSession) config.getUserProperties().get(WebSocketSessionConfigurator.Session);
        String userId = (String) session.getAttribute("login_id");
        // 페이지 새로고침 시 웹소켓 세션 갱신
        userAndSession.put(userId, socketSession);

        // 방에 입장
        List<String> listUsers = null;
        HashSet<String> setUsers = null;
        // 방이 없으면 생성 후, 참가
        if(roomAndUserList.get(roomName) == null){
            listUsers = new ArrayList<>();
            setUsers = new HashSet<>();
            roomAndParticipantsCount.put(roomName, roomAndParticipantsCount.getOrDefault(roomName, 1));
            listUsers.add(userId);
            setUsers.add(userId);
        }else{
            setUsers = roomAndUserSet.get(roomName);
            listUsers = roomAndUserList.get(roomName);
        }

        // 방이 있으면 해당 방에 본인이 있는지 확인
        if(!setUsers.contains(userId)){
            setUsers.add(userId);
            listUsers.add(userId);
        }



        // 갱신된 정보를 다시 맵에 넣음
        roomAndUserList.put(roomName, listUsers);
        roomAndUserSet.put(roomName, setUsers);
    }
    // 클라이언트로 부터 메시지가 오면 handleMessage가 호출 된다.
    @OnMessage
    public void handleMessage(String message, Session userSession, @PathParam("roomName") String roomName) {
        System.out.println("onMessage:"+roomName);
        List<String> list = roomAndUserList.get(roomName);
        for(int i=0; i < list.size(); i++){
            String userName = list.get(i);
            Session userSocketSession = userAndSession.get(userName);
            if(userSocketSession.equals(userSession)) continue;

            try {
                userSocketSession.getBasicRemote().sendText(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @OnClose
    public void handleClose(Session userSession, @PathParam("roomName") String roomName) {
        System.out.println("client is now disconnected...");
//        HttpSession session = (HttpSession) configs.get(userSession).getUserProperties().get(WebSocketSessionConfigurator.Session);
//        String userId = (String) session.getAttribute("login_id");
//        System.out.println("finish:"+userId);
        System.out.println("roomName:"+roomName);


        roomAndParticipantsCount.put(roomName, roomAndParticipantsCount.get(roomName)-1);
    }
    @OnError
    public void handleError(Throwable e, Session userSession) {
        e.printStackTrace();
    }
}
