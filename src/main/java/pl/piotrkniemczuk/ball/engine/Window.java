package pl.piotrkniemczuk.ball.engine;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {

    /**
     * Window handle
     */
    private Long windowHandle;


    /**
     * Constructor
     * @param width window width
     * @param height window height
     * @param title window title
     * @throws Exception throw exception if failed create instance
     */
    public Window(int width, int height, String title){
        this(width, height, title, false);
    }

    public Window(int width, int height, String title, boolean fullscreen) {
        try {
            InitializeGLFW();
            InitializeWindow(width, height, title, fullscreen);
            InitializeOpenGL();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize GLFW: OpenGL Version 3.3, Core Profile, Hidden Window, Not Resizable
     * @throws Exception throw exception if failed initialize glfw
     */
    private void InitializeGLFW() throws Exception {
        if(!glfwInit())
            throw new Exception("Failed to initialize GLFW");

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
    }

    /**
     * Initialize Window
     * @param width window width
     * @param height window height
     * @param title window title
     * @throws Exception throw exception if failed create window
     */
    private void InitializeWindow(int width, int height, String title, boolean fullscreen) throws Exception {
        this.windowHandle = glfwCreateWindow(width, height, title, fullscreen ? glfwGetPrimaryMonitor() : NULL, NULL);

        if(this.windowHandle == NULL)
            throw new Exception("Failed to create window");

        glfwMakeContextCurrent(this.windowHandle);
    }

    /**
     * Basic settings: Create Capabilities, Enable Depth Test and Disable Cursor
     */
    private void InitializeOpenGL(){
        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        glfwSetInputMode(this.windowHandle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    /**
     * Get Window handle
     * @return window handle
     */
    public Long getWindow(){
        return this.windowHandle;
    }

    /**
     * Check if window is open
     * @return window should close is false(then window is open)
     */
    public boolean isOpen(){
        return !glfwWindowShouldClose(this.windowHandle);
    }

    /**
     * Swap Buffers, Poll Events and set glClear on Color and Dept Buffer Bit
     */
    public void display(){
        glfwSwapBuffers(this.windowHandle);
        glfwPollEvents();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public Vector2i getWindowSize(){
        Vector2i size = null;
        try(MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            glfwGetWindowSize(this.windowHandle, w, h);
            size = new Vector2i(w.get(), h.get());
        }
        return size;
    }

    /**
     * Show window
     */
    public void show(){
        glfwShowWindow(this.windowHandle);
    }


    /**
     * Window should close
     * @param should close or not
     */
    public void shouldClose(boolean should) {
        glfwSetWindowShouldClose(this.windowHandle, should);
    }

    /**
     * Close window
     */
    public void shouldClose(){
        shouldClose(true);
    }

    /**
     * Destroy window and Terminate GLFW
     */
    public void clearMemory(){
        glfwDestroyWindow(this.windowHandle);
        glfwTerminate();
    }
}
