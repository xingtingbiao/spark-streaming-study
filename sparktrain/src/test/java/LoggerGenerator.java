import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * 日志生成类
 */
public class LoggerGenerator {
    private static final Logger log = Logger.getLogger(LoggerGenerator.class.getName());
    @Test
    public void logGenerator() throws InterruptedException {
        int index = 0;
        while (true) {
            Thread.sleep(1000);
            log.info("current value is : " + index++);
        }
    }
}
