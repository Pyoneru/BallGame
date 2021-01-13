package pl.piotrkniemczuk.ball.objects;

import org.joml.Matrix4f;
import pl.piotrkniemczuk.ball.assets.Assets;
import pl.piotrkniemczuk.ball.assets.Mesh;
import pl.piotrkniemczuk.ball.assets.Shader;
import pl.piotrkniemczuk.ball.engine.Collider;
import pl.piotrkniemczuk.ball.graphics.Color;
import pl.piotrkniemczuk.ball.graphics.Graphic;

import static org.lwjgl.glfw.GLFW.*;

public class Point extends Collider {

    private boolean collected;
    private Mesh mesh;
    private Color color;
    private Shader shader;
    private float speed;

    public Point(Color color){
        super();
        Assets assets = Assets.getInstance();
        mesh = assets.getModel("cube");
        shader = assets.getShader("Color");
        this.color = color;

        rotation.set(
                (float)Math.toRadians(20.0f),
                (float)Math.toRadians(15.0f),
                (float)Math.toRadians(30.0f)
        );
        scale.set(0.1f);
        position.y = 2.0f;
        speed = 30.0f;
        collected = false;
    }

    public void update(float delta){
        this.rotation.z = (float)Math.toRadians(glfwGetTime() * speed * 0.5f);
        this.rotation.y = (float)Math.toRadians(glfwGetTime() * speed);
        this.rotation.x = (float)Math.toRadians(glfwGetTime() * speed * 0.8f);
    }

    public void render(){
        shader.bind();
        shader.setModel(this);
        shader.setMat4(Graphic.getInstance().getCamera().getCameraMatrix(), "Camera");
        shader.setMat4(new Matrix4f().rotateXYZ(this.rotation), "Rotation");
        shader.setVec3(color.toVec3(), "Color");
        shader.setLight(Graphic.getInstance().getLight());
        shader.setVec3(Graphic.getInstance().getCamera().getPosition(), "CameraPos");
        shader.setInteger(0, "EnableTexture");
        mesh.render();
        shader.unbind();
    }

    @Override
    public void enter(Collider collider) {
        if(collider.getTag().equals("Camera")){
            collected = true;
        }
    }

    @Override
    public void collision(Collider collider) {
    }

    @Override
    public void exit(Collider collider) {
    }

    @Override
    public String getTag() {
        return "Point";
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
