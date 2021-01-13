package pl.piotrkniemczuk.ball.engine;

import org.joml.Vector3f;

public class Transform {

    public Vector3f position;
    public Vector3f rotation;
    public Vector3f scale;

    public Transform(Vector3f position, Vector3f rotation, Vector3f scale){
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Transform(){
        this(new Vector3f(), new Vector3f(), new Vector3f(1.0f, 1.0f, 1.0f));
    }
}
