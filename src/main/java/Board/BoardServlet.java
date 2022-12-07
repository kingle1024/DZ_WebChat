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
    PrintWriter out;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("BoardServlet doGet > ");
        String requestURI = request.getRequestURI();
        BoardDAO boardDAO;
        try {
            boardDAO = new BoardDAO();
            out = response.getWriter();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        switch (requestURI) {
            case "/board/notice": {
                List<Board> boardsList = boardDAO.list("");

                request.setAttribute("boardsList", boardsList);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/notice/list.jsp");

                dispatcher.forward(request, response);

                break;
            }
            case "/board/notice/view":{
                String no = request.getParameter("no");
                System.out.println(no);
                Board board = boardDAO.viewBoard(no);
                boardDAO.addHit(no);
                System.out.println(board);
                request.setAttribute("board", board);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/notice/view.jsp");
                dispatcher.forward(request, response);

                break;
            }

            default:
                System.out.println("requestURI : " + requestURI);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("BoardServlet doPost > ");
        String requestURI = request.getRequestURI();
        BoardDAO boardDAO;

        try {
            boardDAO = new BoardDAO();
            out = response.getWriter();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        switch (requestURI) {
            case "/board/notice/insert":{
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
                    String jsonStr = in.readLine();
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    HttpSession session = request.getSession();
                    String title = (String) jsonObject.get("title");
                    String writer = (String) session.getAttribute("login_name");
                    String content = (String) jsonObject.get("content");
                    Board board = Board.builder()
                            .btitle(title)
                            .bwriter(writer)
                            .bcontent(content)
                            .bhit(0)
                            .bdate(LocalDateTime.now())
                            .build();
                    boolean result = boardDAO.insert(board);
                    JSONObject jsonResult = new JSONObject();
                    if (!result) {
                        jsonResult.put("status", false);
                        jsonResult.put("message", "등록 실패");
                    } else {
                        jsonResult.put("status", true);
                        jsonResult.put("url", "/board/notice");
                        jsonResult.put("message", "등록 성공");
                    }
                    System.out.println(jsonResult);
                    out.println(jsonResult);
                    break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            default : {

            }
        }
    }
}
