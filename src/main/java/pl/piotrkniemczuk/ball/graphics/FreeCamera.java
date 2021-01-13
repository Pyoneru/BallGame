package pl.piotrkniemczuk.ball.graphics;

import org.joml.*;
import org.joml.Math;
import pl.piotrkniemczuk.ball.engine.Collider;
import pl.piotrkniemczuk.ball.engine.Input;
import pl.piotrkniemczuk.ball.engine.Window;
import static org.lwjgl.glfw.GLFW.*;
import static pl.piotrkniemczuk.ball.engine.Input.*;

/**
 * Implementation of Camera Interface. Free Cam(like as First Person Camera)
 */
public class FreeCamera extends Camera {

    protected Vector3f cameraFront;
    protected Vector3f cameraUp;
    protected Vector3f cameraRight;

    protected Vector2f lastMousePos;

    protected Vector2i windowSize;

    public float sensitivity;
    public float velocity;

    private float pitch;
    private float yaw;

    public FreeCamera(Vector3f cameraPosition, Window window){
        super(cameraPosition, new Vector3f(), new Vector3f(1.0f, 1.0f, 1.0f));
        this.cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        this.cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.cameraRight = new Vector3f();

        this.windowSize = window.getWindowSize();
        this.lastMousePos = getMousePos();

        this.sensitivity = 0.05f;
        this.velocity = 1.0f;

        this.pitch = 0.0f;
        this.yaw = 0.0f;
    }

    public FreeCamera(Window window){
        this(new Vector3f(), window);
    }

    @Override
    public Matrix4f getCameraMatrix() {
        Vector3f sum = new Vector3f();
        position.add(cameraFront, sum);
        return new Matrix4f().perspective((float)Math.toRadians(90.0f), (float)(windowSize.x/windowSize.y), 0.001f, 1000.0f)
                .lookAt(position, sum, cameraUp);
    }

    @Override
    public void update(float delta) {
        InputUpdate(delta);
        CameraUpdate();
    }

    @Override
    public Vector3f getPosition() {
        return this.position;
    }

    /**
     * Update camera input
     * @param delta delta time
     */
    protected void InputUpdate(float delta){
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

        if(isKeyDown(GLFW_KEY_SPACE)){
            position.add(new Vector3f(0.0f, velocity, 0.0f), position);
        }else if(isKeyDown(GLFW_KEY_LEFT_CONTROL)){
            position.add(new Vector3f(0.0f, -velocity, 0.0f), position);
        }

    }

    /**
     * Update mouse move
     */
    protected void CameraUpdate(){
        Vector2f mousePos = getMousePos();
        float xoffset = mousePos.x - lastMousePos.x;
        float yoffset = lastMousePos.y - mousePos.y;
        lastMousePos = mousePos;

        xoffset *= sensitivity;
        yoffset *= sensitivity;

        yaw += xoffset;
        pitch += yoffset;

        if(pitch > 89.0f)
            pitch = 89.0f;
        else if(pitch < -89.0f)
            pitch = -89.0f;
        // ----------------------------
        Vector3f direction = new Vector3f();
        direction.x = (float)Math.cos(Math.toRadians(pitch)) * (float)Math.cos(Math.toRadians(yaw));
        direction.y = (float)Math.sin(Math.toRadians(pitch));
        direction.z = (float)Math.cos(Math.toRadians(pitch)) * (float)Math.sin(Math.toRadians(yaw));
        direction.normalize(cameraFront);

        cameraFront.cross(new Vector3f(0.0f, 1.0f, 0.0f), cameraRight);
        cameraRight.normalize(cameraRight);
        cameraRight.cross(cameraFront, cameraUp);
        cameraUp.normalize(cameraUp);
    }
    // getters and setters
    public Vector2i getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(Vector2i windowSize) {
        this.windowSize = windowSize;
    }

    public Vector3f getCameraPosition() {
        return position;
    }

    public Vector3f getCameraFront() {
        return cameraFront;
    }

    public Vector3f getCameraRight() {
        return cameraRight;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    @Override
    public void enter(Collider collider) {
    }

    @Override
    public void collision(Collider collider) {
    }

    @Override
    public void exit(Collider collider) {
    }

    @Override
    public String getTag() {
        return "Camera";
    }
}
