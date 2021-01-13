package pl.piotrkniemczuk.ball.test;

import org.joml.Matrix4f;
import pl.piotrkniemczuk.ball.assets.Mesh;
import pl.piotrkniemczuk.ball.assets.Shader;
import pl.piotrkniemczuk.ball.assets.Texture;
import pl.piotrkniemczuk.ball.engine.Transform;
import pl.piotrkniemczuk.ball.engine.Window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

import static pl.piotrkniemczuk.ball.engine.Input.*;

public class TextureTest {

    public TextureTest(){
        Window window = new Window(1280, 720, "Texture Test");
        glViewport(0, 0, 1280, 720);

        float[] vertices = {
                -0.6f, 1.0f, 0.0f,
                -0.6f, -1.0f, 0.0f,
                0.6f, -1.0f, 0.0f,
                0.6f, 1.0f, 0.0f
        };

        float[] textCoord = {
          0.0f, 1.0f,
          0.0f, 0.0f,
          1.0f, 0.0f,
          1.0f, 1.0f
        };

        int[] indices = {
                0, 1, 2,
                2, 3, 0
        };

        Mesh mesh = new Mesh(vertices, textCoord, new float[0], indices);
        Shader shader = Shader.create()
                .attachShader("./Assets/Shaders/test/TextureVertex.glsl", GL_VERTEX_SHADER)
                .attachShader("./Assets/Shaders/test/TextureFragment.glsl", GL_FRAGMENT_SHADER)
                .link();
        Texture texture = new Texture("./Assets/Textures/image.png");

        Transform transform = new Transform();
        transform.position.z -= 3.0f;

        Matrix4f projection = new Matrix4f().perspective((float)Math.toRadians(45.0f), (float)(1280/720), 0.1f, 100.0f);

        glfwShowWindow(window.getWindow());
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        while(window.isOpen()){

            if(isKeyPressed(GLFW_KEY_ESCAPE))
                glfwSetWindowShouldClose(window.getWindow(), true);


            shader.bind();
            shader.setMat4(projection, "Projection");
            shader.setModel(transform);
            texture.bind();
            mesh.render();
            texture.unbind();
            window.display();
        }

        shader.clearMemory();
        mesh.clearMemory();
        window.clearMemory();
    }

    public static void main(String[] args) {
        new TextureTest();
    }
}
