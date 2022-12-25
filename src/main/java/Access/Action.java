package Access;

import Custom.RQ;
import Member.MemberDAO;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class Action {

    @RQ(url = "/search")
    public String search(HttpServletRequest request, HttpServletResponse response){
        return "/jsp/access/search.jsp";
    }

    @RQ(url = "/register")
    public String register(HttpServletRequest request, HttpServletResponse response){
        MemberDAO memberDAO = new MemberDAO();
        long beforeTime = System.currentTimeMillis();
        request.setAttribute("sds", memberDAO.sd());
        long afterTime = System.currentTimeMillis();
        long secDiffTime = (afterTime - beforeTime); //두 시간에 차 계산
        System.out.println("시간차이(m) : "+secDiffTime);

        return "/jsp/access/register.jsp";
    }

    @RQ(url = "/sgg")
    public JSONObject sgg(HttpServletRequest request, HttpServletResponse response){
        MemberDAO memberDAO = new MemberDAO();
        String sgg = request.getParameter("sgg");
        List<String> result = memberDAO.sgg(sgg);

        JSONObject jsonResult = new JSONObject();
        if (result == null) {
            jsonResult.put("status", true);
            jsonResult.put("message", "데이터 가져오기 실패");
        } else {
            jsonResult.put("status", false);
            jsonResult.put("sgg", result);
        }
        return jsonResult;
    }
    @RQ(url = "/sdName")
    public JSONObject sdName(HttpServletRequest request, HttpServletResponse response){
        MemberDAO memberDAO = new MemberDAO();
        String sgg = request.getParameter("sdName");
        List<String> result = memberDAO.sdName(sgg);
        JSONObject jsonResult = new JSONObject();
        if (result == null) {
            jsonResult.put("status", true);
            jsonResult.put("message", "데이터 가져오기 실패");
        } else {
            jsonResult.put("status", false);
            jsonResult.put("sdName", result);
        }
        return jsonResult;
    }
}
