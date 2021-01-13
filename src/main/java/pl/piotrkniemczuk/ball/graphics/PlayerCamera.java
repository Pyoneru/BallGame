package pl.piotrkniemczuk.ball.graphics;

import org.joml.Vector3f;
import pl.piotrkniemczuk.ball.engine.Input;
import pl.piotrkniemczuk.ball.engine.Window;

import static org.lwjgl.glfw.GLFW.*;
import static pl.piotrkniemczuk.ball.engine.Input.*;

public class PlayerCamera extends FreeCamera {

    public PlayerCamera(Vector3f cameraPosition, Window window) {
        super(cameraPosition, window);
    }

    public PlayerCamera(Input input, Window window) {
        super(window);
    }

    @Override
    protected void InputUpdate(float delta) {
        float y = position.y;
        float velocity = this.velocity * delta;

        if(isKeyDown(GLFW_KEY_LEFT_SHIFT)){
            velocity *= 10;
        }

        if(isKeyDown(GLFW_KEY_W)){
            Vector3f mul = new Vector3f();
            cameraFront.mul(velocity, mul);
            position.add(mul, position);
        }else if(isKeyDown(GLFW_KEY_S)){
            Vector3f mul = new Vector3f();
            cameraFront.mul(-velocity, mul);
            position.add(mul, position);
        }

        if(isKeyDown(GLFW_KEY_A)){
            Vector3f mul = new Vector3f();
            cameraRight.mul(-velocity, mul);
            position.add(mul, position);
        }else if(isKeyDown(GLFW_KEY_D)){
            Vector3f mul = new Vector3f();
            cameraRight.mul(velocity, mul);
            position.add(mul, position);
        }

        position.y = y;
    }
}
