package callback;

import lombok.extern.slf4j.Slf4j;

/**
 * @author tianxing
 */
@Slf4j
public class Client {
    public static void main(String[] args) {
        A a = new A();
        Thread thread = new Thread(() -> a.run(i -> {
            log.info("执行回调函数");
            return i;
        }, 1, 2));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("执行结果:{}", a.getRes());
    }
}
