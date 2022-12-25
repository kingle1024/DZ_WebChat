package Admin;

import Board.BoardService;
import Custom.RQ;
import Member.Member;
import Member.MemberDAO;
import Member.MemberService;
import Page.PageUtil;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class Action {
    boolean isAdmin;
    MemberDAO memberDAO = new MemberDAO();
    BoardService boardService = new BoardService();
    MemberService memberService = new MemberService();
    public String memberList(HttpServletRequest request, HttpServletResponse response){
        validAdmin(request, response);
        String search = request.getParameter("search");
        String pageIndex = request.getParameter("pageIndex");

        PageUtil pageUtil = boardService.pageUtil(search, pageIndex, "", "member");

        request.setAttribute("member", pageUtil.getList());
        request.setAttribute("pager", pageUtil.paper());

        return "/jsp/admin/list.jsp";
    }

    public JSONObject memberUserstatusNouse(HttpServletRequest request, HttpServletResponse response){
        validAdmin(request, response);
        String id = request.getParameter("id");
        JSONObject jsonResult = new JSONObject();

        if(memberService.userStatus(id, "nouse")){
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

        if(memberService.userStatus(id, "use")){
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

        if(memberService.userStatus(id, "withdraw")){
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

    @RQ(url = "/search")
    public JSONObject search(HttpServletRequest request, HttpServletResponse response){
        String query = request.getParameter("query");
        JSONObject jsonResult = new JSONObject();
        System.out.println("query:"+query);

        List<Member> member = memberService.search(query);

        if(member != null) {
            jsonResult.put("status", true);
            jsonResult.put("member", member);
        }else{
            jsonResult.put("status", false);
        }

        return jsonResult;
    }
}
