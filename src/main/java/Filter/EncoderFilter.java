package Filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
@WebFilter(
        urlPatterns = "/*",
        initParams = {
//                @WebInitParam(name="no.auth.urls", value="/WEB-INF/jsp/login.jsp"),
                @WebInitParam(name="no.auth.urls", value="/login"),
                @WebInitParam(name="login.url", value="/"),
                @WebInitParam(name="encoding", value="utf-8")
        }
)
public class EncoderFilter implements Filter {

    ServletContext context;
    String loginUrl = "";
    Set<String> authUrls = new HashSet<>();

    public void init(FilterConfig fConfig) {
        System.out.println("EncodingFilter.init() : utf-8 Encoding.....");
        context = fConfig.getServletContext();
        String context = fConfig.getServletContext().getContextPath();
        loginUrl = context + fConfig.getInitParameter("login.url");
        authUrls.add("/");
        authUrls.add("/login");
        authUrls.add("/member/dupUidCheck");
        authUrls.add("/member/insert");
        authUrls.add("/member/searchId");
        authUrls.add("/member/searchPwd");
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 서블릿보다 먼저 호출됨
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String context = ((HttpServletRequest) request).getContextPath();
        String pathInfo = ((HttpServletRequest) request).getRequestURI();
        String realPath = request.getRealPath(pathInfo);
        String message = "Context Info :"+context +
                "\n URI Info : "+ pathInfo +
                "\n Physical Path : " + realPath;
        System.out.println(message+"\n");

        /*로그인 된 사용자만 사용 가능한 URL 인지 확인*/
        if(isAuth(req, pathInfo)) {
            chain.doFilter(request, response);
//            System.out.println("작업 시간 : " + (end - begin) + "ms");
        }else {
            System.out.println("loginUrl :"+loginUrl);
            resp.sendRedirect(loginUrl);
        }
    }
    private boolean isAuth(HttpServletRequest req, String pathInfo) {
        HttpSession session = req.getSession();
        Object value = session.getAttribute("isLogon");

        boolean isLogin = value != null ? (Boolean) value : false;

        return (isLogin || pathInfo.contains("/board/normal") || pathInfo.contains("/access")
                 || authUrls.contains(pathInfo));
    }
}
