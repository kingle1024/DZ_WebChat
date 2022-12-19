package Config;

import Custom.RQ;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

public class CustomCofig {
    public void action(HttpServletRequest request, HttpServletResponse response, Map<String, Object> objectMap, Map<String, Method> methodMap) throws ServletException, IOException {
        String path = request.getRequestURI();

        Object obj = objectMap.get(path);
        Method method = methodMap.get(path);

        try {
            if(obj != null && method != null){
                Object ret = method.invoke(obj, request, response);
                // String class가 맞으면
                if (ret.getClass().equals(String.class)){
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF"+ ret);
                    dispatcher.forward(request, response);
                } else if(ret.getClass().equals(JSONObject.class)){
                    JSONObject jsonResult = (JSONObject) ret;
                    PrintWriter out = response.getWriter();
                    out.println(jsonResult == null ? "{status : false}" : jsonResult.toString());
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    public void initAction(
            String methodType,
            Class<?> targetClass,
            Map<String, Object> className2ObjectMap,
            Map<String, Object> objectMap,
            Map<String, Method> methodMap) {

        String packageName = targetClass.getPackage().toString().split(" ")[1];
        String packageNameLower = packageName.toLowerCase();
        // 목록
        try {
            String filePath = packageName;
            if(methodType.contains("POST"))
                filePath += ".ActionPost";
            else
                filePath += ".Action";

            Class<?> cls = Class.forName(filePath); // -> 실제 파일 위치
            // 메소드들을 가져옴
            Method[] methods = cls.getDeclaredMethods();
            for(Method method : methods){
                String methodName = method.getName();
                String action = "/";
                if(method.isAnnotationPresent(RQ.class)){
                    RQ rq = method.getAnnotation(RQ.class);
                    action += packageNameLower;
                    action += rq.url();
                }else {
                    ArrayList<String> actionInfo = splitMethodNames(methodName);
                    StringBuilder sb = new StringBuilder();
                    sb.append("/").append(packageNameLower);
                    for (String ac : actionInfo) {
                        sb.append("/").append(ac);
                    }
                    action = sb.toString();
                }

                methodMap.put(action, method);
                if(!className2ObjectMap.containsKey(filePath)){
                    System.out.println("cls.getDeclaredConstructor():"+filePath);
                    System.out.println(cls);
                    Object object = cls.getDeclaredConstructor().newInstance();
                    className2ObjectMap.put(filePath, object);
                    objectMap.put(action, object);
                }else{
                    objectMap.put(action, className2ObjectMap.get(filePath));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<String> splitMethodNames(String input) {
        int idx = 0;
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            // split 처리
            if(c < 'a') {
                String subStr = input.substring(idx, i).toLowerCase();
                result.add(subStr);
                idx = i;
            }
        }
        result.add(input.substring(idx).toLowerCase());

        return result;
    }


}
