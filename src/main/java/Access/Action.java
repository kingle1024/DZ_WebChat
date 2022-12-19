package Access;

import Custom.RQ;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Action {
    @RQ(url = "/search")
    public String search(HttpServletRequest request, HttpServletResponse response){
        return "/jsp/access/search.jsp";
    }

    @RQ(url = "/register")
    public String register(HttpServletRequest request, HttpServletResponse response){
        return "/jsp/access/register.jsp";
    }
}
