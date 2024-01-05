import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author yinmengqi
 * @version 1.0
 * @date 2023/2/14 12:33
 */
@Slf4j
public class ThreadWaitInterrupt {

    @SneakyThrows
    @Test
    public void waitTest(){
        Thread thread = new Thread(new Runnable() {

            @SneakyThrows
            @Override
            public void run() {
                log.info("开始线程");
                Thread.sleep(1000);
                log.info("业务执行");
                synchronized (this){
                    this.wait();
                    log.info("线程状态{}",Thread.currentThread().getState());
                }
            }
        });
        thread.start();
        Thread.sleep(2000);
        log.info("等待");
        thread.interrupt();
        log.info("中断线程{}",thread.getState());
    }
}
