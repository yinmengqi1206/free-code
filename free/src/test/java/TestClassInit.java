import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yinmengqi
 * @version 1.0
 * @date 2023/2/15 11:41
 */
public class TestClassInit {

    public class ParentClass {
        private int parentX;
        public ParentClass() {
            setX(100);
        }
        public void setX(int x) {
            parentX = x;
        }
    }

    public class ChildClass extends ParentClass{
        private int childX = 1;
        public ChildClass() {}
        @Override
        public void setX(int x) {
            super.setX(x);
            childX = x;
            System.out.println("ChildX 被赋值为 " + x);
        }
        public void printX() {
            System.out.println("ChildX = " + childX);
        }

    }

    @Test
    public void TryInitMain() {
        List list = new ArrayList();
        list.add("123");
        List<Integer> list2 = list;
        System.out.println(list2.get(0).intValue());
        ChildClass cc = new ChildClass();
        cc.printX();
    }
}
