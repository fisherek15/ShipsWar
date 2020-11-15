import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

class BoardTest {

    @Test
    void processCommand() {
    }

    @Test
    void isShipAvailable() throws Exception{
        Board board = new Board();
        Method method = Board.class.getDeclaredMethod("isShipAvailable", int.class);
        method.setAccessible(true);
        Assertions.assertEquals(false, method.invoke(board, -1));
        Assertions.assertEquals(false, method.invoke(board, 0));
        Assertions.assertEquals(true, method.invoke(board, 1));
        Assertions.assertEquals(true, method.invoke(board, 4));
        Assertions.assertEquals(false, method.invoke(board, 5));

    }

    @Test
    void hasShipAvailableProperValues() throws Exception {
        Board board = new Board();
        Field availableShips = Board.class.getDeclaredField("availableShips");
        availableShips.setAccessible(true);
        Map<Integer, Integer> expected1 = new HashMap<>();
        expected1.put(1, 4);
        expected1.put(2, 3);
        expected1.put(3, 2);
        expected1.put(4, 1);
        Map<Integer, Integer> boardMap = (Map<Integer, Integer>) availableShips.get(board);
        assertThat(expected1, is(boardMap));
    }
}