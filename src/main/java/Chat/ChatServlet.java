package Chat;

import Config.CustomCofig;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
@WebServlet(name = "ChatServlet", value = "/chat/*")
public class ChatServlet extends HttpServlet {
    Map<String, Object> className2ObjectMap = new HashMap<>();
    Map<String, Object> objectMap = new HashMap<>();
    Map<String, Method> methodMap = new HashMap<>();

    CustomCofig customCofig;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // ANS 객체로 빼는 방법은 좋지 않은 방법인가?
        customCofig = new CustomCofig();
        customCofig.initAction("GET", this.getClass(), className2ObjectMap, objectMap, methodMap);
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        customCofig.action(request, response, objectMap, methodMap);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

    }
}
