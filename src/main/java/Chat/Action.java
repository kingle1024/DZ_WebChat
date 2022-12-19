package Chat;

import Custom.RQ;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Action {
    @RQ(url = "/add")
    public String add(HttpServletRequest request, HttpServletResponse response){
        try {
            System.out.println("/chat/add");
            HttpSession session = request.getSession();
            String chatRoomName = request.getParameter("p");
            request.setAttribute("p", chatRoomName);
            request.setAttribute("login_id", session.getAttribute("login_id"));

            return "/jsp/chat/view.jsp";
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @RQ(url = "/list")
    public String list(HttpServletRequest request, HttpServletResponse response){
        try {
            System.out.println("/chat/list");
            return "/jsp/chat/index.jsp";
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
