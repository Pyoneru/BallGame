package pl.piotrkniemczuk.ball.assets;

import pl.piotrkniemczuk.ball.engine.Dispose;
import pl.piotrkniemczuk.ball.engine.GameSettings;
import pl.piotrkniemczuk.ball.utils.OBJLoader;
import pl.piotrkniemczuk.ball.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL33.*;

public class Assets implements Dispose {

    private static Assets instance;
    public static String ASSETS_MODEL_PATH = "./Assets/Models/" ;
    public static String ASSETS_TEXTURE_PATH = "./Assets/Textures/";
    public static String ASSETS_SHADER_PATH = "./Assets/Shaders/";

    public enum ImageType{
        PNG, JPG
    }

    private final Map<String, Shader> shaders;
    private final Map<String, Mesh> meshes;
    private final Map<String, Texture> textures;

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
        this.shaders.put(shader, Shader.create()
        .attachShader(ASSETS_SHADER_PATH + shader + "Vertex.glsl", GL_VERTEX_SHADER)
        .attachShader(ASSETS_SHADER_PATH + shader + "Fragment.glsl", GL_FRAGMENT_SHADER)
        .link());
    }

    public Shader getShader(String shader){
        return this.shaders.get(shader);
    }

    /**
     * Application can only meshes with .obj extension.
     * @param mesh
     */
    public void addModel(String mesh){
        this.meshes.put(mesh, OBJLoader.loadModel(ASSETS_MODEL_PATH + mesh + ".obj"));
    }

    public void addModel(String name, Mesh mesh){
        this.meshes.put(name, mesh);
    }

    public Mesh getModel(String mesh){
        return this.meshes.get(mesh);
    }

    public void addTexture(String texture, ImageType type){
        this.textures.put(texture, new Texture(ASSETS_TEXTURE_PATH + texture + "." + type.toString().toLowerCase()));
    }

    public void addTexture(GameSettings.TexturePair texturePair){
        this.addTexture(texturePair.texture, texturePair.type);
    }

    public Texture getTexture(String texture){
        return this.textures.get(texture);
    }

    @Override
    public void clearMemory(){
        this.shaders.forEach((k, v) -> v.clearMemory());
        this.meshes.forEach((k, v) -> v.clearMemory());
        this.textures.forEach((k, v) -> v.clearMemory());
    }
}