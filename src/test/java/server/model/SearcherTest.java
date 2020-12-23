package server.model;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SearcherTest {

   /* @Test
    public void plantSunflower() {
        Game game = new Game(2, 2, 3,3, Constant.COORDINATE_SHIFT_POINT_X, Constant.COORDINATE_SHIFT_POINT_Y);
        Sunflower sunflower = new Sunflower(new Vector2D(310, 230));

        Assert.assertEquals(true, game.plantSunflower(sunflower.getPosition()));
    }*/

    @Test
    void searcher() throws IOException {
        ArrayList<String> key_words = new ArrayList<>();
        key_words.add("objects");
        String[][] arr_expect = new String[3][2];
        arr_expect[0][0] = "https://www.wideskills.com/java-tutorial/java-classes-and-objects";
        arr_expect[0][1] = "Java Classes and Objects | Wideskills";
        arr_expect[1][0] = "https://stackify.com/what-is-java-garbage-collection/";
        arr_expect[1][1] = "What is Java Garbage Collection? How It Works, Best Practices, Tutorials, and More – Stackify";
        arr_expect[2][0] = "https://www.geeksforgeeks.org/jvm-works-jvm-architecture/?ref=lbp";
        arr_expect[2][1] = "How JVM Works - JVM Architecture? - GeeksforGeeks";

        assertArrayEquals(arr_expect, Searcher.searcher(key_words));
    }

}
