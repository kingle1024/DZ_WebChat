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
    MemberDAO memberDAO;
    PrintWriter out;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        System.out.println("MemberServlet doGet > " + requestURI);
        JSONObject jsonResult = new JSONObject();
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("login_id");
        System.out.println(id);
        try {
            memberDAO = new MemberDAO();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        switch (requestURI) {
            case "/member/withdraw":{
                out = response.getWriter();
                if(memberDAO.userStatus_withdraw(id)){
                    jsonResult.put("message", "정지 성공");
                    jsonResult.put("status", true);
                    jsonResult.put("url", "/logout");
                }else{
                    jsonResult.put("message", "정지 실패");
                    jsonResult.put("status", false);
                }
                out.println(jsonResult);
                break;
            }
            case "/member/view": {
                Member member = memberDAO.viewMember(id);
                request.setAttribute("member", member);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/view.jsp");

                dispatcher.forward(request, response);

                break;
            }

            case "/member/dupUidCheck": {
                Member member = memberDAO.viewMember(id);
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
            case "/member/edit":{
                Member member = memberDAO.viewMember(id);
                request.setAttribute("member", member);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/member/edit.jsp");

                dispatcher.forward(request, response);

                break;
            }
            default:
                System.out.println("requestURI : " + requestURI);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestURI = request.getRequestURI();
        System.out.println("MemberServlet doPost > " + requestURI);
        out = response.getWriter();
        try {
            memberDAO = new MemberDAO();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        switch (requestURI) {
            case "/member/edit":{
                System.out.println("member/edit");
                BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
                String jsonStr = in.readLine();

                JSONObject jsonMember = new JSONObject(jsonStr);
                System.out.println(jsonMember);
                HttpSession session = request.getSession();
                String uid = (String) session.getAttribute("login_id");
                JSONObject jsonResult = new JSONObject();

                Member member = memberDAO.viewMember(uid);
                String name = (String) jsonMember.get("name");
                String email = (String) jsonMember.get("email");
                System.out.println("email:"+email);
                String phone = (String) jsonMember.get("phone");

                member.setName(name);
                member.setPhone(phone);
                member.setEmail(email);
                boolean result = memberDAO.edit(member);
                if(!result){
                    jsonResult.put("status", false);
                    jsonResult.put("message", "수정 실패");
                }else {
                    jsonResult.put("status", true);
                    jsonResult.put("url", "view");
                    jsonResult.put("message", "수정 성공");
                }

                out.println(jsonResult);
                break;
            }
            case "/member/searchId":{
                BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
                String jsonStr = in.readLine();

                JSONObject jsonObject = new JSONObject(jsonStr);
                String name = (String) jsonObject.get("name");
                String email = (String) jsonObject.get("email");
                Member m = Member.builder()
                        .name(name)
                        .email(email)
                        .build();
                System.out.println("m:"+m);
                Member member = memberDAO.searchId(m);

                JSONObject jsonResult = new JSONObject();
                if(member != null) {
                    jsonResult.put("status", true);
                    jsonResult.put("message", "회원님의 아이디는 " + member.getUserId() + " 입니다.");
                }else{
                    jsonResult.put("status", false);
                    jsonResult.put("message", "일치하는 정보가 없습니다.");
                }
                out.println(jsonResult);
            }
            case "/member/searchPwd":{
                BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
                String jsonStr = in.readLine();

                JSONObject jsonObject = new JSONObject(jsonStr);
                System.out.println(jsonStr);
                System.out.println(jsonObject);
                String id = (String) jsonObject.get("userid");
                String phone = (String) jsonObject.get("phone");
                Member m = Member.builder()
                        .userId(id)
                        .phone(phone)
                        .build();

                Member member = memberDAO.searchPwd(m);
                System.out.println(member);
                JSONObject jsonResult = new JSONObject();
                if(member != null) {
                    jsonResult.put("status", true);
                    jsonResult.put("message", "회원님의 비밀번호는 " + member.getPwd() + " 입니다.");
                }else{
                    jsonResult.put("status", false);
                    jsonResult.put("message", "일치하는 정보가 없습니다.");
                }
                out.println(jsonResult);
            }
            case "/member/insert":
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
                    jsonResult.put("url", "/jsp/index.jsp");
                    jsonResult.put("message", "회원가입 성공");
                } catch (Member.ExistMember e) {
                    jsonResult.put("status", false);
                    jsonResult.put("message", "해당 아이디는 현재 사용중인 아이디입니다.");
                    throw new RuntimeException(e);
                }

                out.println(jsonResult);
                break;
        }


    }
}
