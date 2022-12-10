package Chat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ChatServlet", value = "/chat/*")
public class ChatServlet extends HttpServlet {
    PrintWriter out;
    RequestDispatcher dispatcher;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        System.out.println("BoardServlet doGet > " + requestURI);
        HttpSession session = request.getSession();
        out = response.getWriter();


        switch (requestURI) {
            case "/chat/add":{
                System.out.println("/chat/add");
                String chatRoomName = request.getParameter("p");
                request.setAttribute("p", chatRoomName);
                request.setAttribute("login_id", session.getAttribute("login_id"));
                dispatcher = request.getRequestDispatcher("/jsp/chat/view.jsp");
                dispatcher.forward(request, response);
                break;
            }
            case "/chat/list":{
                System.out.println("/chat/list");
                dispatcher = request.getRequestDispatcher("/jsp/chat/index.jsp");
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
        String requestURI = request.getRequestURI();
        System.out.println("BoardServlet doPost > " + requestURI);

        try {
            out = response.getWriter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
