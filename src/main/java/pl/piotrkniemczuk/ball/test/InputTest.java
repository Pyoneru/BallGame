package pl.piotrkniemczuk.ball.test;

import pl.piotrkniemczuk.ball.engine.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

/**
 * Input Test class
 */
public class InputTest {

    public InputTest(){
        try {
            Window window = new Window(800, 600, "Input Test");
            Input input = new Input(window);

            glfwShowWindow(window.getWindow());
            glClearColor(0.14f, 0.57f, 0.78f, 1.0f);
            while(window.isOpen()){

                /*
                Key Down
                 */
                if(input.isKeyDown(GLFW_KEY_A))
                    System.out.println("A");
                if(input.isKeyDown(GLFW_KEY_D))
                    System.out.println("D");

                /*
                Key Pressed
                 */
                if(input.isKeyPressed(GLFW_KEY_W))
                    System.out.println("W");
                if(input.isKeyPressed(GLFW_KEY_S))
                    System.out.println("S");
                if(input.isKeyPressed(GLFW_KEY_ESCAPE))
                    glfwSetWindowShouldClose(window.getWindow(), true);
                window.display();
            }

            window.clearMemory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new InputTest();
    }
}
