package pl.piotrkniemczuk.ball.graphics;

import org.joml.Matrix4f;
import pl.piotrkniemczuk.ball.assets.Assets;
import pl.piotrkniemczuk.ball.assets.Mesh;
import pl.piotrkniemczuk.ball.assets.Shader;
import pl.piotrkniemczuk.ball.assets.Texture;
import pl.piotrkniemczuk.ball.engine.GameAnalyze;
import pl.piotrkniemczuk.ball.objects.GameObject;
import pl.piotrkniemczuk.ball.utils.Utils;

public class Graphic {

    private static Graphic instance;

    private Shader objectShader;
    private Camera camera;

    //          Light
    private boolean enableLight;
    private Light light;
    private Shader lightShader;
    private Mesh lightMesh;
    private float lightScale;
    // --------------------------

    private GameAnalyze ga;

    private Graphic(){
        ga = GameAnalyze.getInstance();
        Assets assets = Assets.getInstance();
        objectShader = assets.getShader("Object");
        lightShader = assets.getShader("Light");
        lightMesh = assets.getModel("cube");
    }

    public static Graphic getInstance(){
        if(instance == null)
            instance = new Graphic();
        return instance;
    }

    public void update(float delta){
        camera.update(delta);
    }

    public void render(){
        // Render Light
        if(enableLight) RenderLight();
    }

    private void RenderLight(){
        long time = Utils.getTime();
        lightShader.bind();
        lightShader.setMat4(camera.getCameraMatrix(), "Camera");
        lightShader.setMat4(new Matrix4f()
                .translate(light.getPosition())
                .scale(lightScale), "Model");
        lightShader.setVec3(light.getColor().toVec3(), "Color");
        lightMesh.render();
        lightShader.unbind();
        ga.timeLight += (Utils.getTime() - time);
    }

    public void render(GameObject object){
        Texture texture = object.getTexture();
        int enableTexture = object.isTexture();

        objectShader.bind();
        objectShader.setInteger(enableTexture, "EnableTexture");
        if(enableTexture == 1) texture.bind();
        objectShader.setMat4(camera.getCameraMatrix(), "Camera");
        objectShader.setModel(object);
        objectShader.setMat4(new Matrix4f().rotateXYZ(object.rotation), "Rotation");
        objectShader.setVec3(camera.getPosition(), "CameraPos");
        objectShader.setLight(light);
        objectShader.setMaterial(object.getMaterial());
        object.getMesh().render();
        if(enableTexture == 1) texture.unbind();
        objectShader.unbind();
    }

    public void render(GameObject object, Shader shader){
        Texture texture = object.getTexture();
        int enableTexture = object.isTexture();

        shader.bind();
        shader.setInteger(enableTexture, "EnableTexture");
        if(enableTexture == 1) texture.bind();
        shader.setMat4(camera.getCameraMatrix(), "Camera");
        shader.setModel(object);
        shader.setVec3(camera.getPosition(), "CameraPos");
        shader.setLight(light);
        shader.setMaterial(object.getMaterial());
        object.getMesh().render();
        if(enableTexture == 1) texture.unbind();
        shader.unbind();
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public boolean isEnableLight() {
        return enableLight;
    }

    public void setEnableLight(boolean enableLight) {
        this.enableLight = enableLight;
    }

    public Mesh getLightMesh() {
        return lightMesh;
    }

    public void setLightMesh(Mesh lightMesh) {
        this.lightMesh = lightMesh;
    }

    public Light getLight() {
        return light;
    }

    public void setLight(Light light) {
        this.light = light;
    }

    public float getLightScale() {
        return lightScale;
    }

    public void setLightScale(float lightScale) {
        this.lightScale = lightScale;
    }
}
