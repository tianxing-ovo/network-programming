package callback;

import lombok.extern.slf4j.Slf4j;

/**
 * 异步计算器
 *
 * @author tianxing
 */
@Slf4j
public class AsyncCalculator {
    public void run(int a, int b, ResultCallback callback) {
        log.info("开始计算...");
        try {
            // 模拟耗时操作
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int sum = a + b;
        log.info("计算结束!");
        if (callback != null) {
            callback.onResult(sum);
        }
    }
}
