package pl.piotrkniemczuk.ball.test;

import pl.piotrkniemczuk.ball.graphics.Color;
import pl.piotrkniemczuk.ball.assets.Mesh;
import pl.piotrkniemczuk.ball.assets.Shader;
import pl.piotrkniemczuk.ball.assets.Texture;
import pl.piotrkniemczuk.ball.engine.Input;
import pl.piotrkniemczuk.ball.engine.Transform;
import pl.piotrkniemczuk.ball.engine.Window;
import pl.piotrkniemczuk.ball.graphics.Camera;
import pl.piotrkniemczuk.ball.graphics.FreeCamera;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

/**
 * Test Camera
 */
public class CameraTest {

    private Window window;
    private Input input;
    private Mesh mesh;
    private Shader shader;
    private Camera camera;
    private Texture texture;
    private int size;
    private Transform[] transforms;
    private Color[] colors;

    private void Init(){
        this.window = new Window(1280, 720, "Camera Test");
        this.camera = new FreeCamera(window);
        FreeCamera freeCamera = (FreeCamera) camera;
        freeCamera.velocity = 5f;
        InitAssets();
        glfwShowWindow(this.window.getWindow());
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void InitAssets(){
        float[] vertices = {
          -1.0f, 1.0f, 0.0f,
          -1.0f, -1.0f, 0.0f,
          1.0f, -1.0f, 0.0f,
          1.0f, 1.0f, 0.0f
        };

        int[] indices = {
          0, 1, 2,
          2, 3, 0
        };

        float[] textureCoordinates = {
          0.0f, 1.0f,
          0.0f, 0.0f,
          1.0f, 0.0f,
          1.0f, 1.0f
        };

        this.mesh = new Mesh(vertices, textureCoordinates, new float[0], indices);

        this.shader = Shader.create()
                .attachShader("./Assets/Shaders/test/CameraVertex.glsl",GL_VERTEX_SHADER)
                .attachShader("./Assets/Shaders/test/CameraFragment.glsl", GL_FRAGMENT_SHADER)
                .link();

        this.texture = new Texture("./Assets/Textures/image.png");

        this.size = 6;
        this.transforms = new Transform[this.size];
        for(int i = 0; i < this.size; i++) transforms[i] = new Transform();
        this.colors = new Color[this.size];
        // Front
        transforms[0].position.z -= 10.0f;
        colors[0] = Color.Red;

        // Back
        transforms[1].position.z += 10.0f;
        colors[1] = Color.Blue;

        // Left
        transforms[2].position.x -= 10.0f;
        transforms[2].rotation.y -= (float)Math.toRadians(90.0f);
        colors[2] = Color.Green;

        // Right
        transforms[3].position.x += 10.0f;
        transforms[3].rotation.y += (float)Math.toRadians(90.0f);
        colors[3] = Color.Yellow;

        // Down
        transforms[4].position.y -= 10.0f;
        transforms[4].rotation.x -= (float)Math.toRadians(90.0f);
        colors[4] = Color.Pink;

        // Up
        transforms[5].position.y += 10.0f;
        transforms[5].rotation.x += (float)Math.toRadians(90.0f);
        colors[5] = Color.Magenta;

        for(int i = 0; i < this.size; i++)
            transforms[i].scale.mul(10.0f);

    }

    private void Update(float delta){
        camera.update(delta);
        if(input.isKeyPressed(GLFW_KEY_ESCAPE))
            glfwSetWindowShouldClose(this.window.getWindow(), true);
    }

    private void Render(){
        shader.bind();
        texture.bind();
        for(int i = 0; i < this.size; i++){
            shader.setMat4(camera.getCameraMatrix(), "Camera");
            shader.setModel(transforms[i]);
            shader.setVec3(colors[i].toVec3(), "Color");
            mesh.render();
        }
        texture.unbind();
        shader.unbind();
    }

    private void ClearMemory(){
        texture.clearMemory();
        shader.clearMemory();
        mesh.clearMemory();
        window.clearMemory();
    }

    public CameraTest(){
        Init();

        double lastTime = glfwGetTime();
        while(window.isOpen()){
            double currentTime = glfwGetTime();
            double delta = currentTime - lastTime;
            if(delta > 1.0/60){
                lastTime = currentTime;
                Update((float)delta);
            }

            Render();

            window.display();
        }
        ClearMemory();
    }

    public static void main(String[] args) {
        new CameraTest();
    }
}
