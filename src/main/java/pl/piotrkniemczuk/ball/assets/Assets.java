package pl.piotrkniemczuk.ball.assets;

import pl.piotrkniemczuk.ball.utils.OBJLoader;
import pl.piotrkniemczuk.ball.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL33.*;

public class Assets {

    private static Assets instance;

    public enum ImageType{
        PNG, JPG
    }

    private Map<String, Shader> shaders;
    private Map<String, Mesh> meshes;
    private Map<String, Texture> textures;

    private Assets(){
        this.shaders = new HashMap<>();
        this.meshes = new HashMap<>();
        this.textures = new HashMap<>();
    }

    public static Assets getInstance(){
        if(instance == null)
            instance = new Assets();
        return instance;
    }

    public void addShader(String shader){
        long time = Utils.getTime();
        this.shaders.put(shader, Shader.create()
        .attachShader("./Assets/Shaders/" + shader + "Vertex.glsl", GL_VERTEX_SHADER)
        .attachShader("./Assets/Shaders/" + shader + "Fragment.glsl", GL_FRAGMENT_SHADER)
        .link());
        System.out.println("Create Shader: " + (Utils.getTime() - time));
    }

    public Shader getShader(String shader){
        return this.shaders.get(shader);
    }

    public void addModel(String mesh){
        long time = Utils.getTime();
        this.meshes.put(mesh, OBJLoader.loadModel("./Assets/Models/" + mesh + ".obj"));
        System.out.println("Create Mesh(and load model): " + (Utils.getTime() - time));
    }

    public void addModel(String name, Mesh mesh){
        this.meshes.put(name, mesh);
    }

    public Mesh getModel(String mesh){
        return this.meshes.get(mesh);
    }

    public void addTexture(String texture, ImageType type){
        long time = Utils.getTime();
        this.textures.put(texture, new Texture("./Assets/Textures/" + texture + "." + type.toString().toLowerCase()));
        System.out.println("Create Texture: " + (Utils.getTime()- time));
    }

    public Texture getTexture(String texture){
        return this.textures.get(texture);
    }

    public void clearMemory(){
        long time = Utils.getTime();
        this.shaders.forEach((k, v) -> v.clearMemory());
        this.meshes.forEach((k, v) -> v.clearMemory());
        this.textures.forEach((k, v) -> v.clearMemory());
        System.out.println("Delete all Assets: " + (Utils.getTime() - time));
    }
}