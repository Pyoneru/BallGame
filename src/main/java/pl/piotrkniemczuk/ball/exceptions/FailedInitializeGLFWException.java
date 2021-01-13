package pl.piotrkniemczuk.ball.exceptions;

public class FailedInitializeGLFWException extends Exception {

    public FailedInitializeGLFWException(){
        super("Failed to initialize GLFW");
    }
}
