package Auth;

import Member.Member;
import Member.MemberDAO;
import Member.MemberRepository;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

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
            HttpSession session = request.getSession();
            String user_id = request.getParameter("user_id");
            if(user_id == null){
                user_id = (String) session.getAttribute("login_id");
            }

            MemberRepository dao = new MemberDAO();
            Member member = dao.findById(user_id);

            RequestDispatcher dispatch;
            validMember(request, response, member);

            member.setLoginDateTime(LocalDateTime.now());
            dao.edit(member);

            session.setAttribute("isLogon", true);
            session.setAttribute("login_id", user_id);
            session.setAttribute("login_name", member.getName());
            session.setAttribute("login_admin", member.isAdmin());

            dispatch = request.getRequestDispatcher("/jsp/index.jsp");
            dispatch.forward(request, response);
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }
    }

    private static void validMember(HttpServletRequest request, HttpServletResponse response, Member m) throws ServletException, IOException {
        RequestDispatcher dispatch;
        if(m == null){
            request.setAttribute("errorMsg", "아이디 및 비밀번호를 확인하세요.");
            dispatch = request.getRequestDispatcher("/jsp/login.jsp");
            dispatch.forward(request, response);
        }else if(m.getUserStatus().equals("STOP")){
            request.setAttribute("errorMsg", "정지된 회원입니다.");
            dispatch = request.getRequestDispatcher("/jsp/login.jsp");
            dispatch.forward(request, response);
        }
    }
}
