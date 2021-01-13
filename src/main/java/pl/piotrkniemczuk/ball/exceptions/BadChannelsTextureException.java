package pl.piotrkniemczuk.ball.exceptions;

public class BadChannelsTextureException extends RuntimeException {

    public BadChannelsTextureException(int channels){
        super("Can not recognize channels value: " + channels);
    }
}
