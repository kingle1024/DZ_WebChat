package Custom;

import java.lang.reflect.Method;

public class test {
    public static void main(String[] args) throws ClassNotFoundException {
        test t = new test();
        t.solution();
    }
    public void solution() throws ClassNotFoundException {
        // People Class의 정보를 읽어오기 위해 forName에 위치를 지정해준다.
        Class<?> cls = Class.forName("Custom.People");
        // 해당 클래스에 있는 메소드 목록을 가져온다.
        Method[] methods = cls.getDeclaredMethods();
        for(Method method : methods){
            // 만약 RQ 어노테이션이 존재하면
            if(method.isAnnotationPresent(RQ.class)){
                RQ rq = method.getAnnotation(RQ.class);
                // RQ로 선언한 url 값을 가져온다.
                System.out.println(rq.url() +" "+method.getName());
            }
        }
    }
}
