package pl.piotrkniemczuk.ball.engine;

import org.joml.Vector2f;
import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Input {

    /**
     * Window handle
     */
    private Long window;

    /**
     * Variable saved pressed key from callback
     */
    private int keyPressed;

    private Vector2f cursorPosition;

    private Vector2f scroll;

    /**
     * Constructor, set Key Callback
     * @param window get window handle from Window instance
     */
    public Input(Window window){
        this.window = window.getWindow();

        this.keyPressed = -1;
        glfwSetKeyCallback(this.window, (win, key, scancode, action, mods) -> {
           if(action == GLFW_PRESS) this.keyPressed = key;
        });

        this.cursorPosition = new Vector2f();
        glfwSetCursorPosCallback(this.window, (win, x, y) ->{
           this.cursorPosition.set((float)x, (float)y);
        });

        this.scroll = new Vector2f();
        glfwSetScrollCallback(this.window, (win, x, y) -> {
            this.scroll.set((float)x, (float)y);
        });

    }

    /**
     * Return true when key is down(how long key is down, how long return true)
     * @param key key code
     * @return true if press
     */
    public boolean isKeyDown(int key){
        return glfwGetKey(this.window, key) == GLFW_PRESS;
    }

    /**
     * Return true when key was pressed(check only one time click
     * @param key key code
     * @return true if key code is equal to keyPressed, then then keyPressed is clear(set value on -1)
     */
    public boolean isKeyPressed(int key){
        boolean is = this.keyPressed == key;
        if(is) this.keyPressed = -1;
        return is;
    }

    public Vector2f getCursorPos(){
        return this.cursorPosition;
    }

    public Vector2f getMousePos(){
        Vector2f mousePos = null;
        try(MemoryStack stack = MemoryStack.stackPush()){
            DoubleBuffer x = stack.mallocDouble(1);
            DoubleBuffer y = stack.mallocDouble(1);
            glfwGetCursorPos(this.window, x, y);
            mousePos = new Vector2f((float)x.get(), (float)y.get());
        }
        return mousePos;
    }

    public Vector2f getScroll(){
        Vector2f s = new Vector2f(this.scroll);
        this.scroll.set(0.0f, 0.0f);
        return s;
    }

    public boolean isMouseButtonDown(int key){
        return glfwGetMouseButton(this.window, key) == GLFW_PRESS;
    }
}
