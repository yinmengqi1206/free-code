import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author yinmengqi
 * @version 1.0
 * @date 2023/1/31 11:48
 */
@Slf4j
public class heart {

    @Test
    public void test() {
        //单心
        heart(15, 0.9, "love");
        //双心
        heartTwo(15, 0.9, "爱", "尹孟琦", "孙艳娜");
        //点缀
        heartTwoWithXK(15, 0.9, "爱", "尹孟琦", "孙艳娜");
    }

    public void heart(int r, double size, String req) {
        size = 1 / (1.5 * r * size);
        StringBuilder sb = new StringBuilder();
        for (int y = r; y > -r; y--, sb.append("\n"))
            for (int x = -2 * r; x < 2 * r; x++) {
                char msg = (req + req).charAt((x - y) % req.length() + req.length());
                sb.append((inHeart(size, x, y) ? msg + " " : " "));
            }
        System.err.println(sb.toString());
    }

    public void heartTwo(int r, double size, String center, String left, String right) {
        size = 1 / (1.5 * r * size);
        StringBuilder sb = new StringBuilder();
        for (int y = r; y > -r; y--, sb.append("\n"))
            for (int x = -2 * r; x < 4 * r; x++) {
                boolean isLeft = inHeart(size, x, y);
                boolean isRight = inHeart(size, x - 25, y - 3);
                //双空格
                String req = null;
                if (isLeft && isRight) req = center;
                else if (isLeft) req = left;
                else if (isRight) req = right;
                if (req != null) sb.append((req + req).charAt((x - y) % req.length() + req.length()));
                else sb.append(" ");//双空格
            }
        System.err.println(sb.toString());
    }

    public void heartTwoWithXK(int r, double size, String center, String left, String right) {
        size = 1 / (1.5 * r * size);
        StringBuilder sb = new StringBuilder();
        for (int y = r; y >= -r; y--, sb.append("\n"))
            for (int x = -2 * r; x <= 4 * r; x++) {
                boolean isLeft = inHeart(size, x, y + 3);
                boolean isRight = inHeart(size, x - 25, y);
                //双空格
                String req = null;
                String w = "";
                if (isLeft && isRight) req = center;
                else if (isLeft) req = left;
                else if (isRight) req = right;
                else if ((y == -r || y == r)) {
                    if (x < 3 * r - 7) {
                        req = "♥";
                        w = " ";
                    }
                } else if (x == 4 * r || x == -2 * r || line(x, y + 3)) req = "♥";
                if (req != null) sb.append((req + req).charAt((x - y) % req.length() + req.length()) + w);
                else sb.append(" ");//双空格
            }
        System.err.println(sb.toString());
    }

    public boolean inHeart(double size, int x, int y) {
        return Math.pow(Math.pow(x * size, 2) + Math.pow(y * 2 * size, 2) - 1, 3) - Math.pow(x * size, 2) * Math.pow(y * 2 * size, 3) <= 0;
    }

    public boolean line(int x, int y) {
        return 4 * y - x == 0;
    }
}
