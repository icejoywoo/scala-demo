package ajava.call.scala;

public class InvokeScalaDemo {
    public static void main(String[] args) {
        ScalaUtils.log("Hello");
        ScalaUtils$.MODULE$.log("World");

        System.out.println(ScalaUtils.MAX_LOG_SIZE());
        System.out.println(ScalaUtils$.MODULE$.MAX_LOG_SIZE());

        // 函数调用
//        int r = FunctionUtil.testFunction();
    }
}
