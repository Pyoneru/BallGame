package pl.piotrkniemczuk.ball.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import pl.piotrkniemczuk.ball.engine.Collider;

/**
 * Camera interface
 */
public abstract class Camera extends Collider {

    public Camera(Vector3f position, Vector3f rotation, Vector3f scale) {
        super(position, rotation, scale);
    }

    public Camera() {
    }

    /**
     * Calculate Camera Matrix(projection, look at...)
     * @return Camera Matrix
     */
    public abstract Matrix4f getCameraMatrix();

    /**
     * Update camera(Input, Move...)
     * @param delta delta time
     */
    public abstract void update(float delta);

    /**
     * Get Camera Position
     * @return Camera Position in game world
     */
    public abstract Vector3f getPosition();
}
