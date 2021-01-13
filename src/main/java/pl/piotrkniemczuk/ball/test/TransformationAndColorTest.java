package pl.piotrkniemczuk.ball.test;

import org.joml.Matrix4f;
import pl.piotrkniemczuk.ball.graphics.Color;
import pl.piotrkniemczuk.ball.assets.Mesh;
import pl.piotrkniemczuk.ball.engine.Transform;
import pl.piotrkniemczuk.ball.assets.Shader;
import pl.piotrkniemczuk.ball.engine.Window;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.glfw.GLFW.*;
import static pl.piotrkniemczuk.ball.engine.Input.*;

/**
 * Test Transform And Color
 */
public class TransformationAndColorTest {

    private Window window;

    public TransformationAndColorTest(){
        window = new Window(1280, 720, "Transformation and Color Test");

        float[] vertices = {
                -0.5f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f, 0.5f, 0.0f
        };

        int[] indices = {
                0, 1, 2,
                2, 3, 0
        };

        Mesh mesh = new Mesh(vertices, new float[0], new float[0], indices);
        Shader shader = Shader.create()
                .attachShader("./Assets/Shaders/test/TaCVertex.glsl", GL_VERTEX_SHADER)
                .attachShader("./Assets/Shaders/test/TaCFragment.glsl", GL_FRAGMENT_SHADER)
                .link();

        Transform transform = new Transform();
        transform.position.z -= 2.0f;
        Color color = Color.Magenta;

        glViewport(0, 0, 1280, 720);

        Matrix4f projection = new Matrix4f().perspective((float)Math.toRadians(45.0f), (float)(1280/720), 0.1f, 100.0f);

        glfwShowWindow(window.getWindow());
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        double lastTime = glfwGetTime();
        while(window.isOpen()){
            double currentTime = glfwGetTime();
            if(currentTime - lastTime > 1.0f/30) {
                lastTime = currentTime;
                Input(transform);
            }

            shader.bind();
            shader.setMat4(projection, "Projection");
            shader.setVec3(color.toVec3(), "Color");
            shader.setModel(transform);
            mesh.render();
            window.display();
        }

        window.clearMemory();
    }

    private void Input(Transform transform){
        if(isKeyPressed(GLFW_KEY_ESCAPE))
            glfwSetWindowShouldClose(window.getWindow(), true);

        float speed = 0.1f;

        // Position
        if(isKeyDown(GLFW_KEY_W))
            transform.position.y += speed;
        if(isKeyDown(GLFW_KEY_S))
            transform.position.y -= speed;
        if(isKeyDown(GLFW_KEY_A))
            transform.position.x -= speed;
        if(isKeyDown(GLFW_KEY_D))
            transform.position.x += speed;
        if(isKeyDown(GLFW_KEY_Z))
            transform.position.z += speed;
        if(isKeyDown(GLFW_KEY_X))
            transform.position.z -= speed;

        // Rotation
        if(isKeyDown(GLFW_KEY_Q))
            transform.rotation.x -= speed;
        if(isKeyDown(GLFW_KEY_E))
            transform.rotation.x += speed;
        if(isKeyDown(GLFW_KEY_R))
            transform.rotation.y -= speed;
        if(isKeyDown(GLFW_KEY_T))
            transform.rotation.y += speed;
        if(isKeyDown(GLFW_KEY_Y))
            transform.rotation.z -= speed;
        if(isKeyDown(GLFW_KEY_U))
            transform.rotation.z += speed;

        // Scale
        if(isKeyDown(GLFW_KEY_1))
            transform.scale.x -= speed;
        if(isKeyDown(GLFW_KEY_2))
            transform.scale.x += speed;
        if(isKeyDown(GLFW_KEY_3))
            transform.scale.y -= speed;
        if(isKeyDown(GLFW_KEY_4))
            transform.scale.y += speed;
        if(isKeyDown(GLFW_KEY_5))
            transform.scale.z -= speed;
        if(isKeyDown(GLFW_KEY_6))
            transform.scale.z += speed;
    }

    public static void main(String[] args) {
        new TransformationAndColorTest();
    }
}
