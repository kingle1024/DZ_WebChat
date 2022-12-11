package Board;

import org.json.JSONObject;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
@WebServlet(name = "BoardServlet", value = "/board/*")
public class BoardServlet extends HttpServlet {
    BoardDAO boardDAO;
    PrintWriter out;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        System.out.println("BoardServlet doGet > " + requestURI);

        HttpSession session = request.getSession();

        try {
            boardDAO = new BoardDAO();
            out = response.getWriter();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        switch (requestURI) {
            case "/board/notice/edit": {
                String bno = request.getParameter("bno");
                String loginId = (String) session.getAttribute("login_id");

                Board board = boardDAO.viewBoard(bno);

                if(board.getBwriterId().equals(loginId)){
                    request.setAttribute("board", board);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/notice/edit.jsp");
                    dispatcher.forward(request, response);
                }

                break;
            }


            case "/board/normal/list":{
                String search = request.getParameter("search");
                if(search == null) search = "";
                String type = "normal";
                List<Board> boardsList = boardDAO.list(search, type);
                request.setAttribute("boardsList", boardsList);
                request.setAttribute("type", type);

                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/normal/list.jsp");
                dispatcher.forward(request, response);

                break;
            }
            case "/board/list":{
                String search = request.getParameter("search");
                if(search == null) search = "";
                String type = request.getParameter("type");
                List<Board> boardsList = boardDAO.list(search, type);
                request.setAttribute("boardsList", boardsList);
                request.setAttribute("type", type);

                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/notice/list.jsp");
                dispatcher.forward(request, response);

                break;
            }
            case "/board/normal/view":{
                String no = request.getParameter("no");
                boardDAO.addHit(no);
                Board board = boardDAO.viewBoard(no);
                request.setAttribute("board", board);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/normal/view.jsp");
                dispatcher.forward(request, response);
                break;
            }
            case "/board/notice/view":{
                String no = request.getParameter("no");
                boardDAO.addHit(no);
                Board board = boardDAO.viewBoard(no);
                request.setAttribute("board", board);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/notice/view.jsp");
                dispatcher.forward(request, response);

                break;
            }
            case "/board/del":{
                String bno = request.getParameter("bno");
                Board board = boardDAO.viewBoard(bno);
                String loginId = (String) session.getAttribute("login_id");
                JSONObject jsonResult = new JSONObject();
                if(!board.getBwriterId().equals(loginId)){
                    jsonResult.put("message", "글쓴이가 아니면 삭제할 수 없습니다.");
                    jsonResult.put("status", false);
                }else if(!boardDAO.del(bno)){
                    jsonResult.put("message", "삭제 실패");
                    jsonResult.put("status", false);
                }else{
                    jsonResult.put("message", "삭제 성공");
                    jsonResult.put("status", true);
                }
                out.println(jsonResult);
            }

            default:
                System.out.println("requestURI : " + requestURI);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        System.out.println("BoardServlet doPost > " + requestURI);
        BoardDAO boardDAO;

        try {
            boardDAO = new BoardDAO();
            out = response.getWriter();
        } catch (NamingException | IOException e) {
            throw new RuntimeException(e);
        }

        switch (requestURI) {
            case "/board/normal/insert":{
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
                    String jsonStr = in.readLine();
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    String type = (String) jsonObject.get("type");

                    Board board = Board.builder()
                            .btitle((String) jsonObject.get("title"))
                            .bwriter((String) jsonObject.get("writer"))
                            .bcontent((String) jsonObject.get("content"))
                            .type(type)
                            .build();
                    boolean result = boardDAO.insert(board);

                    JSONObject jsonResult = new JSONObject();

                    if (!result) {
                        jsonResult.put("status", false);
                        jsonResult.put("message", "등록 실패");
                    }else {
                        jsonResult.put("status", true);
                        jsonResult.put("url", "/board/list?type="+type);
                        jsonResult.put("message", "등록 성공");
                    }
                    System.out.println(jsonResult);
                    out.println(jsonResult);
                    break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
            case "/board/insert":{
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
                    String jsonStr = in.readLine();
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    HttpSession session = request.getSession();
                    String title = (String) jsonObject.get("title");
                    String writer = (String) session.getAttribute("login_name");
                    String writerId = (String) session.getAttribute("login_id");
                    String content = (String) jsonObject.get("content");
                    String type = (String) jsonObject.get("type");

                    // TODO 등록한 계정이 관리자가 아닐 때에 바로 리턴 처리
//                    int isAdmin = () session.getAttribute("login_admin");

                    Board board = Board.builder()
                            .btitle(title)
                            .bwriter(writer)
                            .bcontent(content)
                            .bhit(0)
                            .bdate(LocalDateTime.now())
                            .type(type)
                            .bwriterId(writerId)
                            .build();
                    boolean result = boardDAO.insert(board);
                    JSONObject jsonResult = new JSONObject();


                    if (!result) {
                        jsonResult.put("status", false);
                        jsonResult.put("message", "등록 실패");
                    }else {
                        jsonResult.put("status", true);
                        jsonResult.put("url", "/board/list?type="+type);
                        jsonResult.put("message", "등록 성공");
                    }
                    System.out.println(jsonResult);
                    out.println(jsonResult);
                    break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case "/board/notice/edit":{
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
                    String jsonStr = in.readLine();

                    JSONObject jsonObject = new JSONObject(jsonStr);
                    HttpSession session = request.getSession();
                    String uid = (String) session.getAttribute("login_id");
                    JSONObject jsonResult = new JSONObject();

                    int bno = (int) jsonObject.get("bno");
                    Board board = boardDAO.viewBoard(String.valueOf(bno));

                    String btitle = (String) jsonObject.get("btitle");
                    String bcontent = (String) jsonObject.get("bcontent");
                    System.out.println("bcontent:"+bcontent);

                    board.setBtitle(btitle);
                    board.setBcontent(bcontent);

                    boolean result = boardDAO.edit(board);
                    if(!result && !uid.equals(board.getBwriterId())){
                        jsonResult.put("status", false);
                        jsonResult.put("message", "수정 실패");
                    }else{
                        jsonResult.put("status", true);
                        jsonResult.put("message", "수정 성공");
                        jsonResult.put("url", "/board/notice/view?no="+bno);
                    }
                    out.println(jsonResult);
                    break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case "/board/normal/del": {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
                    String jsonStr = in.readLine();

                    JSONObject jsonObject = new JSONObject(jsonStr);
                    String password = (String) jsonObject.get("password");
                    int bno = (int) jsonObject.get("bno");
                    boolean validPassword = boardDAO.findByNoAndPassword(String.valueOf(bno), password);
                    JSONObject jsonResult = new JSONObject();

                    if(!validPassword){
                        jsonResult.put("status", false);
                        jsonResult.put("message", "패스워드가 틀립니다.");
                    }else if(!boardDAO.del(String.valueOf(bno))){
                        jsonResult.put("message", "삭제 실패");
                        jsonResult.put("status", false);
                    }else{
                        jsonResult.put("message", "삭제 성공");
                        jsonResult.put("status", true);
                        jsonResult.put("url", "board/normal/list?type=normal");
                    }

                    out.println(jsonResult);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            default : {

            }
        }
    }
}
