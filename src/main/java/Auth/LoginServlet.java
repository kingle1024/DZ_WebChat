package Auth;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet(name = "login", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        doHandle(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        doHandle(request, response);
    }

    private void doHandle(HttpServletRequest request, HttpServletResponse response){
        try {
            String user_id = request.getParameter("user_id");
            String user_pw = request.getParameter("user_pw");
            Member member = new Member();
            member.setUserId(user_id);
            member.setPwd(user_pw);

            MemberDAO dao = new MemberDAO();
            boolean result = dao.isExisted(member);
            RequestDispatcher dispatch;
            if(result){
                HttpSession session = request.getSession();
                session.setAttribute("isLogon", true);
                session.setAttribute("login.id", user_id);

                request.setAttribute("id", user_id);
                request.setAttribute("name", member.getName());

                dispatch = request.getRequestDispatcher("/index.jsp");
                dispatch.forward(request, response);
            }else{
                request.setAttribute("errorMsg", "아이디 및 비밀번호를 확인하세요.");
                dispatch = request.getRequestDispatcher("/login.jsp");
                dispatch.forward(request, response);
            }
        } catch (NamingException | SQLException | IOException | ServletException e) {
            throw new RuntimeException(e);
        }
    }
}
