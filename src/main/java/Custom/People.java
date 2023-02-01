package Custom;

public class People {
    @RQ(url = "test")
    public void method1(){
        System.out.println("test");
    }

    @RQ(url = "add")
    public void method2(){
        System.out.println("add");
    }
}
