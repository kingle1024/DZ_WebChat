package Member;

import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class ActionPost {
    MemberDAO memberDAO = new MemberDAO();
    public JSONObject edit(HttpServletRequest request, HttpServletResponse response){
        try {
            System.out.println("member/edit");
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));

            String jsonStr = in.readLine();

            JSONObject jsonMember = new JSONObject(jsonStr);
            System.out.println(jsonMember);
            HttpSession session = request.getSession();
            String uid = (String) session.getAttribute("login_id");
            JSONObject jsonResult = new JSONObject();

            Member member = memberDAO.findById(uid);
            String name = (String) jsonMember.get("name");
            String email = (String) jsonMember.get("email");
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
                jsonResult.put("url", "/member/view");
                jsonResult.put("message", "수정 성공");
            }

            return jsonResult;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject searchId(HttpServletRequest request, HttpServletResponse response){
        try {
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
            return jsonResult;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject searchPwd(HttpServletRequest request, HttpServletResponse response){
        try {
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
            if (member != null) {
                jsonResult.put("status", true);
                jsonResult.put("message", "회원님의 비밀번호는 " + member.getPwd() + " 입니다.");
            } else {
                jsonResult.put("status", false);
                jsonResult.put("message", "일치하는 정보가 없습니다.");
            }
            return jsonResult;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public JSONObject insert(HttpServletRequest request, HttpServletResponse response){
        JSONObject jsonResult = new JSONObject();

        try{
            System.out.println("member/insert");
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            String jsonStr = in.readLine();

            JSONObject jsonMember = new JSONObject(jsonStr);
            String uid = (String) jsonMember.get("id");
            String pwd = (String) jsonMember.get("pwd");
            String name = (String) jsonMember.get("userName");
            String email = (String) jsonMember.get("email");
            String phone = (String) jsonMember.get("phone");

            Member member = Member.builder()
                    .userId(uid)
                    .pwd(pwd)
                    .name(name)
                    .phone(phone)
                    .email(email)
                    .isAdmin(false)
                    .userStatus("USE")
                    .createdate(LocalDateTime.now())
                    .build();

            boolean result = memberDAO.insertMember(member);
            if (result) {
                jsonResult.put("status", true);
                jsonResult.put("url", "/jsp/login.jsp");
                jsonResult.put("message", "회원가입 성공");
            } else {
                jsonResult.put("status", false);
                jsonResult.put("message", "회원가입 실패");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jsonResult;
    }
}
