package pl.piotrkniemczuk.ball.engine;

import org.joml.Vector2f;
import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;
// ToDo: Make Input methods static
public class Input {

    /**
     * Window handle
     */
    private static Long windowHandle;

    /**
     * Variable saved pressed key from callback
     */
    private static int keyPressed;

    private static Vector2f cursorPosition;

    private static Vector2f scroll;


    private Input(){}

    /**
     * Initializer, set Key Callback
     * @param window get window handle from Window instance
     */
    public static void init(Window window){
        windowHandle = window.getWindow();

        keyPressed = -1;
        glfwSetKeyCallback(windowHandle, (win, key, scancode, action, mods) -> {
            if(action == GLFW_PRESS) keyPressed = key;
        });

        cursorPosition = new Vector2f();
        glfwSetCursorPosCallback(windowHandle, (win, x, y) ->{
            cursorPosition.set((float)x, (float)y);
        });

        scroll = new Vector2f();
        glfwSetScrollCallback(windowHandle, (win, x, y) -> {
            scroll.set((float)x, (float)y);
        });
    }

    /**
     * Return true when key is down(how long key is down, how long return true)
     * @param key key code
     * @return true if press
     */
    public static boolean isKeyDown(int key){
        return glfwGetKey(windowHandle, key) == GLFW_PRESS;
    }

    /**
     * Return true when key was pressed(check only one time click
     * @param key key code
     * @return true if key code is equal to keyPressed, then then keyPressed is clear(set value on -1)
     */
    public static boolean isKeyPressed(int key){
        boolean is = keyPressed == key;
        if(is) keyPressed = -1;
        return is;
    }

    public static Vector2f getCursorPos(){
        return cursorPosition;
    }

    public static Vector2f getMousePos(){
        Vector2f mousePos = null;
        try(MemoryStack stack = MemoryStack.stackPush()){
            DoubleBuffer x = stack.mallocDouble(1);
            DoubleBuffer y = stack.mallocDouble(1);
            glfwGetCursorPos(windowHandle, x, y);
            mousePos = new Vector2f((float)x.get(), (float)y.get());
        }
        return mousePos;
    }

    public static Vector2f getScroll(){
        Vector2f s = new Vector2f(scroll);
        scroll.set(0.0f, 0.0f);
        return s;
    }

    public static boolean isMouseButtonDown(int key){
        return glfwGetMouseButton(windowHandle, key) == GLFW_PRESS;
    }
}
