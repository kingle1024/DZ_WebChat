package Member;

import Auth.Member;
import Auth.MemberDAO;
import org.json.JSONObject;

import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@WebServlet(name = "MemberServlet", value = "/member/*")
public class MemberServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("MemberServlet doGet > ");
        String requestURI = request.getRequestURI();

        switch (requestURI) {
            case "/member/view": {
                HttpSession session = request.getSession();
                String id = (String) session.getAttribute("login_id");

                try {
                    MemberDAO memberDAO = new MemberDAO();
                    Member member = memberDAO.viewMember(id);
                    request.setAttribute("member", member);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/view.jsp");

                    dispatcher.forward(request, response);
                } catch (NamingException e) {
                    throw new RuntimeException(e);
                }
                break;
            }

            case "/member/dupUidCheck": {
                String id = request.getParameter("id");

                MemberDAO memberDAO;
                try {
                    memberDAO = new MemberDAO();
                } catch (NamingException e) {
                    throw new RuntimeException(e);
                }

                Member member = memberDAO.viewMember(id);
                JSONObject jsonResult = new JSONObject();
                System.out.println("dupUidCheck");
                System.out.println(member);

                if (member == null) {
                    jsonResult.put("status", true);
                    jsonResult.put("message", "해당 아이디는 사용 가능합니다.");
                } else {
                    jsonResult.put("status", false);
                    jsonResult.put("message", "해당 아이디는 사용 불가능합니다.");
                }
                PrintWriter out = response.getWriter();
                out.println(jsonResult);
                break;
            }
            default:
                System.out.println("requestURI : " + requestURI);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("MemberServlet doPost > ");

        String requestURI = request.getRequestURI();
        if (requestURI.equals("/member/insert")) {
            System.out.println("member/insert");
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            String jsonStr = in.readLine();

            JSONObject jsonMember = new JSONObject(jsonStr);
            String uid = (String) jsonMember.get("id");
            String pwd = (String) jsonMember.get("pwd");
            String name = (String) jsonMember.get("userName");
            String email = (String) jsonMember.get("email");
            String phone = (String) jsonMember.get("phone");

            JSONObject jsonResult = new JSONObject();
            try {
                MemberDAO memberDAO = new MemberDAO();
                Member member = Member.builder()
                        .userId(uid)
                        .pwd(pwd)
                        .name(name)
                        .phone(phone)
                        .email(email)
                        .isAdmin(false)
                        .userStatus(MemberCode.MEMBER_STATUS_ING)
                        .createdate(LocalDateTime.now())
                        .build();

                memberDAO.insertMember(member);
                jsonResult.put("status", true);
                jsonResult.put("url", "index.jsp");
                jsonResult.put("message", "회원가입 성공");
            } catch (NamingException e) {
                jsonResult.put("status", false);
                jsonResult.put("message", "해당 아이디는 현재 사용중인 아이디입니다.");
            } catch (Member.ExistMember e) {
                throw new RuntimeException(e);
            }
            PrintWriter out = response.getWriter();
            out.println(jsonResult);
        }
    }
}
