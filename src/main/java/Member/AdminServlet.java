package Member;

import Auth.Member;
import Auth.MemberDAO;
import org.json.JSONObject;

import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "AdminServlet", value = "/admin/*")
public class AdminServlet extends HttpServlet {
    RequestDispatcher dispatch;
    MemberDAO memberDAO;
    boolean isAdmin;
    PrintWriter out;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("AdminServlet doGet > ");
        String requestURI = request.getRequestURI();
        try {
            memberDAO = new MemberDAO();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        switch (requestURI) {
            case "/admin/memberList" : {
                validAdmin(request, response);
                List<Member> member = memberDAO.list();
                request.setAttribute("member", member);

                dispatch = request.getRequestDispatcher("/listAdmin.jsp");
                dispatch.forward(request, response);

                break;
            }

            case "/admin/member/userStatus_noUse": {
                validAdmin(request, response);
                String id = request.getParameter("id");
                JSONObject jsonResult = new JSONObject();

                out = response.getWriter();
                if(memberDAO.userStatus_noUse(id)){
                    jsonResult.put("message", "미사용 성공");
                    jsonResult.put("status", true);
                }else{
                    jsonResult.put("message", "미사용 실패");
                    jsonResult.put("status", false);
                }
                out.println(jsonResult);

                break;
            }

            case "/admin/member/userStatus_use": {
                validAdmin(request, response);
                String id = request.getParameter("id");
                JSONObject jsonResult = new JSONObject();

                out = response.getWriter();
                if(memberDAO.userStatus_use(id)){
                    jsonResult.put("message", "사용 성공");
                    jsonResult.put("status", true);
                }else{
                    jsonResult.put("message", "사용 실패");
                    jsonResult.put("status", false);
                }
                out.println(jsonResult);

                break;
            }

            case "/admin/member/userStatus_withdraw": {
                validAdmin(request, response);
                String id = request.getParameter("id");
                JSONObject jsonResult = new JSONObject();

                out = response.getWriter();
                if(memberDAO.userStatus_withdraw(id)){
                    jsonResult.put("message", "정지 성공");
                    jsonResult.put("status", true);
                }else{
                    jsonResult.put("message", "정지 실패");
                    jsonResult.put("status", false);
                }
                out.println(jsonResult);

                break;
            }

        }
    }

    private void validAdmin(HttpServletRequest request, HttpServletResponse response) {
        isAdmin = (boolean) request.getSession().getAttribute("login_admin");
        if(!isAdmin) {
            try {
                response.sendRedirect("/");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("AdminServlet doPost > ");

    }
}
