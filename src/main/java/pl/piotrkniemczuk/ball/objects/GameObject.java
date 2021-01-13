package pl.piotrkniemczuk.ball.objects;

import pl.piotrkniemczuk.ball.assets.Material;
import pl.piotrkniemczuk.ball.assets.Mesh;
import pl.piotrkniemczuk.ball.assets.Texture;
import pl.piotrkniemczuk.ball.engine.Transform;

public class GameObject extends Transform {

    private Mesh mesh;
    private Material material;
    private Texture texture;

    public GameObject(Mesh mesh, Material material){
        super();
        this.mesh = mesh;
        this.material = material;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public int isTexture(){
        return (texture != null) ? 1 : 0;
    }
}
