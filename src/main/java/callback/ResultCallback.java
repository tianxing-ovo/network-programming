package callback;

/**
 * 计算结果回调接口
 *
 * @author tianxing
 */
@FunctionalInterface
public interface ResultCallback {
    void onResult(int result);
}
