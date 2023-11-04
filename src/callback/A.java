package callback;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class A {
    int res = -1;

    public int getRes() {
        return res;
    }

    public void run(Callback callback, int a, int b) {
        log.info("开始");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("结束");
        if (callback != null) {
            res = callback.onCallback(a + b);
        }
    }
}
