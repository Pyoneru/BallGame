package pl.piotrkniemczuk.ball.engine;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WindowTest {

    private static Window window;

    @BeforeAll
    public static void before(){
        try {
            window = new Window(800, 600, "Test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void init(){
        assertNotNull(window);
    }

    @Test
    public void getWindow() {
        assertNotNull(window.getWindow());
    }

    @Test
    public void isOpen() {
        assertTrue(window.isOpen());
    }
}