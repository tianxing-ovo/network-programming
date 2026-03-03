package callback;

import lombok.extern.slf4j.Slf4j;

/**
 * @author tianxing
 */
@Slf4j
public class Client {
    public static void main(String[] args) {
        AsyncCalculator calculator = new AsyncCalculator();
        new Thread(() -> calculator.run(1, 2, result -> log.info("计算结果: {}", result))).start();
    }
}
