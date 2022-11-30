package member;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

@WebServlet(name = "SignUpServlet", value = "/SignUpServlet")
public class SignUpServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("name");
        String id = request.getParameter("id");
        String password = request.getParameter("password");
        String mobile_tel1 = request.getParameter("mobile_tel1");
        String mobile_tel2 = request.getParameter("mobile_tel2");
        String mobile_tel3 = request.getParameter("mobile_tel3");
        String email = request.getParameter("email");

        Member member = Member.builder()
                .name(name)
                .userId(id)
                .pwd(password)
                .phone(mobile_tel1 + "-" + mobile_tel2 + "-" + mobile_tel3)
                .email(email)
                .createdate(LocalDateTime.now())
                .build();

        MemberRepositoryDB memberRepositoryDB = new MemberRepositoryDB();
        try {
            memberRepositoryDB.insertMember(member);
        } catch (Member.ExistMember e) {
            throw new RuntimeException(e);
        }
    }
}
