package Admin;

import Member.Member;
import Member.MemberDAO;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class Action {
    boolean isAdmin;
    MemberDAO memberDAO = new MemberDAO();
    public String memberList(HttpServletRequest request, HttpServletResponse response){
        validAdmin(request, response);
        String search = request.getParameter("search");
        if(search == null) search = "";

        List<Member> member = memberDAO.list(search);
        request.setAttribute("member", member);

        return "/jsp/admin/list.jsp";
    }

    public JSONObject memberUserstatusNouse(HttpServletRequest request, HttpServletResponse response){
        validAdmin(request, response);
        String id = request.getParameter("id");
        JSONObject jsonResult = new JSONObject();

        if(memberDAO.userStatus_noUse(id)){
            jsonResult.put("message", "미사용 성공");
            jsonResult.put("status", true);
        }else{
            jsonResult.put("message", "미사용 실패");
            jsonResult.put("status", false);
        }
        return jsonResult;
    }

    public JSONObject memberUserstatusUse(HttpServletRequest request, HttpServletResponse response){
        validAdmin(request, response);
        String id = request.getParameter("id");
        JSONObject jsonResult = new JSONObject();

        if(memberDAO.userStatus_use(id)){
            jsonResult.put("message", "사용 성공");
            jsonResult.put("status", true);
        }else{
            jsonResult.put("message", "사용 실패");
            jsonResult.put("status", false);
        }
        return jsonResult;
    }

    public JSONObject memberUserstatusWithdraw(HttpServletRequest request, HttpServletResponse response){
        validAdmin(request, response);
        String id = request.getParameter("id");
        JSONObject jsonResult = new JSONObject();

        if(memberDAO.userStatus_withdraw(id)){
            jsonResult.put("message", "정지 성공");
            jsonResult.put("status", true);
        }else{
            jsonResult.put("message", "정지 실패");
            jsonResult.put("status", false);
        }
        return jsonResult;
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
}
