package pl.piotrkniemczuk.ball.exceptions;

public class FailedCreateWindowHandlerException extends Exception{

    public FailedCreateWindowHandlerException(){
        super("Failed to create window handler");
    }
}
