package Auth;

import Member.Member;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "login", value = "/login")
public class LoginServlet extends HttpServlet {
    private RequestDispatcher dispatch;
    private HttpSession session;
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
            session = request.getSession();
            String user_id = request.getParameter("user_id");
            String pwd = request.getParameter("user_pw");
            if(user_id == null){
                user_id = (String) session.getAttribute("login_id");
            }

            LoginService loginService = new LoginService();
            Member member = loginService.findById(user_id, pwd);
            validMember(request, response, member);

            dispatch.forward(request, response);
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }
    }

    private void validMember(HttpServletRequest request, HttpServletResponse response, Member member) throws ServletException, IOException {
        if(member == null){
            request.setAttribute("errorMsg", "아이디 및 비밀번호를 확인하세요.");
            dispatch = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
        }else if(member.getUserStatus().equals("STOP")){
            request.setAttribute("errorMsg", "정지된 회원입니다.");
            dispatch = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
        }else {
            session.setAttribute("isLogon", true);
            session.setAttribute("login_id", member.getUserId());
            session.setAttribute("login_name", member.getName());
            session.setAttribute("login_admin", member.isAdmins());
            dispatch = request.getRequestDispatcher("/WEB-INF/jsp/index.jsp");
        }
    }
}
