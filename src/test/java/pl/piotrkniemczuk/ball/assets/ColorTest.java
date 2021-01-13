package pl.piotrkniemczuk.ball.assets;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.junit.jupiter.api.Test;
import pl.piotrkniemczuk.ball.graphics.Color;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @Test
    void add() {
        Color color = Color.Red;
        Color newColor = color.add(Color.Green);
        assertEquals(color, Color.Red);
        assertEquals(newColor, Color.Yellow);
    }

    @Test
    void minus() {
        Color color = Color.White;
        Color newColor = color.minus(Color.Green);
        assertEquals(color, Color.White);
        assertEquals(newColor, Color.Magenta);
    }

    @Test
    void toVec3() {
        Color color = Color.Magenta;
        Vector3f vec3 = color.toVec3();
        assertEquals(vec3, new Vector3f(1.0f, 0.0f, 1.0f));
    }

    @Test
    void toVec4() {
        Color color = Color.Magenta;
        Vector4f vec4 = color.toVec4();
        assertEquals(vec4, new Vector4f(1.0f, 0.0f, 1.0f, 1.0f));
    }
}