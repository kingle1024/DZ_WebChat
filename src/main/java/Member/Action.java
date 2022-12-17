package Member;

import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Action {
    MemberDAO memberDAO = new MemberDAO();
    JSONObject jsonResult = new JSONObject();

    public JSONObject withdraw(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("login_id");
        if(memberDAO.userStatus_withdraw(id)){
            jsonResult.put("message", "정지 성공");
            jsonResult.put("status", true);
            jsonResult.put("url", "/logout");
        }else{
            jsonResult.put("message", "정지 실패");
            jsonResult.put("status", false);
        }
        return jsonResult;
    }

    public String view(HttpServletRequest request){
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("login_id");
        Member member = memberDAO.findById(id);
        request.setAttribute("member", member);
        return "/jsp/view.jsp";
    }

    public JSONObject dupUidCheck(HttpServletRequest request){
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("login_id");
        Member member = memberDAO.findById(id);
        System.out.println("dupUidCheck");

        if (member == null) {
            jsonResult.put("status", true);
            jsonResult.put("message", "해당 아이디는 사용 가능합니다.");
        } else {
            jsonResult.put("status", false);
            jsonResult.put("message", "해당 아이디는 사용 불가능합니다.");
        }
        return jsonResult;
    }
    public String edit(HttpServletRequest request){
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("login_id");
        Member member = memberDAO.findById(id);
        request.setAttribute("member", member);
        return "/jsp/member/edit.jsp";
    }
}
