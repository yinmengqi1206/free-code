import org.junit.jupiter.api.Test;

/**
 * @author yinmengqi
 * @version 1.0
 * @date 2023/2/16 12:02
 */
public class TestClassLoader {

    @Test
    public void getClassLoader(){
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println(systemClassLoader);
        System.out.println(systemClassLoader.getParent());
    }
}
