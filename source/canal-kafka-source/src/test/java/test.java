/**
 * Created by jiangtiteng on 2018/10/12
 */
public class test {
    public static void main(String[] args) throws InterruptedException {
//        ThreadLocal threadLocal = new ThreadLocal();
//        threadLocal.set("223");
//        threadLocal.set("4444");
//
//
//        System.out.println(threadLocal.get());
//        System.out.println(threadLocal.get());


        Object waitObject = new Object();

        synchronized (waitObject) {
            waitObject.wait();
            System.out.println("end");
        }


    }
}
