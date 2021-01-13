package pl.piotrkniemczuk.ball.test;

import pl.piotrkniemczuk.ball.assets.Mesh;
import pl.piotrkniemczuk.ball.assets.Shader;
import pl.piotrkniemczuk.ball.engine.*;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.glfw.GLFW.*;

/**
 * Test Basic Assets
 */
public class AssetsTest {

    public AssetsTest(){
        try {
            Window window = new Window(800, 600, "Assets Test");
            Input input = new Input(window);

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

            /*
            Create Mesh
             */
            Mesh mesh = new Mesh(vertices, new float[0], new float[0], indices);
            /*
            Create Shader
             */
            Shader shader = Shader.create()
                    .attachShader("./Assets/Shaders/test/TestVertex.glsl", GL_VERTEX_SHADER)
                    .attachShader("./Assets/Shaders/test/TestFragment.glsl", GL_FRAGMENT_SHADER)
                    .link();

            glfwShowWindow(window.getWindow());
            glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
            while(window.isOpen()){

                if(input.isKeyPressed(GLFW_KEY_ESCAPE))
                    glfwSetWindowShouldClose(window.getWindow(), true);

                /*
                Render
                 */
                shader.bind();
                mesh.render();
                shader.unbind();

                window.display();
            }

            mesh.clearMemory();
            shader.clearMemory();
            window.clearMemory();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new AssetsTest();
    }
}
