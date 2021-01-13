package pl.piotrkniemczuk.ball.assets;

import org.joml.Vector3f;

/**
 * Material
 */
public class Material {

    public Vector3f ambient;
    public Vector3f diffuse;
    public Vector3f specular;
    public float shininess;

    public static Material Gold = new Material(
            new Vector3f(0.24725f, 0.1995f, 0.0745f),
            new Vector3f(0.75164f, 0.60648f, 0.22648f),
            new Vector3f(0.628281f, 0.555802f, 0.366065f),
            0.4f
    );

    public Material(Vector3f ambient, Vector3f diffuse, Vector3f specular, float shininess) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }

    public Material(){
        this(new Vector3f(), new Vector3f(), new Vector3f(), 32);
    }
}
