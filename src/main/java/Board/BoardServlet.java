package Board;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "BoardServlet", value = "/board/*")
public class BoardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("BoardServlet doGet > ");
        String requestURI = request.getRequestURI();
        BoardDAO boardDAO = null;
        try {
            boardDAO = new BoardDAO();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        switch (requestURI) {
            case "/board/notice": {
                List<Board> boardsList = boardDAO.list("");

                request.setAttribute("boardsList", boardsList);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/notice/list.jsp");

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
                RequestDispatcher dispatcher = request.getRequestDispatcher("/notice/view.jsp");
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

    }
}
