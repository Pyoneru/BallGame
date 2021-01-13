package pl.piotrkniemczuk.ball.graphics;

import org.joml.Vector3f;

/**
 * Light(for Phong model)
 */
public class Light {

    /**
     * Light Pos
     */
    private Vector3f position;
    /**
     * Color light
     */
    private Color color;
    /**
     * Ambient Strength
     */
    public float ambient;
    /**
     * Diffuse Strength
     */
    public float diffuse;
    /**
     * Specular Strength
     */
    public float specular;

    public Light(Vector3f position){
        this(position, Color.White, 0.1f, 1.0f, 0.5f);
    }

    public Light(Vector3f position, Color color, float ambient, float diffuse, float specular) {
        this.position = position;
        this.color = color;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getAmbientStrength() {
        return ambient;
    }

    public void setAmbientStrength(float ambientStrength) {
        this.ambient = ambientStrength;
    }

    public float getSpecular() {
        return specular;
    }

    public void setSpecular(float specular) {
        this.specular = specular;
    }


}
