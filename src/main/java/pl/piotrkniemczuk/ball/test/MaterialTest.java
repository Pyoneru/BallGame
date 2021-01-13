package pl.piotrkniemczuk.ball.test;

import org.joml.Vector3f;
import pl.piotrkniemczuk.ball.assets.Material;
import pl.piotrkniemczuk.ball.assets.Mesh;
import pl.piotrkniemczuk.ball.assets.Shader;
import pl.piotrkniemczuk.ball.engine.Transform;
import pl.piotrkniemczuk.ball.engine.Window;
import pl.piotrkniemczuk.ball.graphics.FreeCamera;
import pl.piotrkniemczuk.ball.graphics.Light;
import pl.piotrkniemczuk.ball.utils.OBJLoader;
import static pl.piotrkniemczuk.ball.engine.Input.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

public class MaterialTest {

    private Window window;

    private FreeCamera camera;

    private Mesh mesh;
    private Shader shaderMesh;
    private Shader shaderLight;

    private Transform transformMesh;
    private Transform transformLight;
    private Material material;

    private Light light;

    private void Init(){
        this.window = new Window(1280, 720, "Material Test");
        this.camera = new FreeCamera(this.window);
        InitAssets();
        InitProperties();
        this.light = new Light(new Vector3f(5.0f, 15.0f, 0.0f));
        this.transformLight.position = this.light.getPosition();
        this.light.ambient = 0.5f;
        window.show();
    }

    private void InitAssets(){
        this.mesh = OBJLoader.loadModel("./Assets/Models/cube.obj");
        this.shaderLight = Shader.create()
                .attachShader("./Assets/Shaders/test/LightSourceVertex.glsl", GL_VERTEX_SHADER)
                .attachShader("./Assets/Shaders/test/LightSourceFragment.glsl", GL_FRAGMENT_SHADER)
                .link();
        this.shaderMesh = Shader.create()
                .attachShader("./Assets/Shaders/test/MaterialVertex.glsl", GL_VERTEX_SHADER)
                .attachShader("./Assets/Shaders/test/MaterialFragment.glsl", GL_FRAGMENT_SHADER)
                .link();
    }
    private void InitProperties(){
        this.transformMesh = new Transform();
        this.transformMesh.position.z -= 10.0f;
        this.transformMesh.position.x -= 5.0f;

        this.transformLight = new Transform();
        this.transformLight.position.y += 15.0f;

        this.material = Material.Gold;
    }

    private void Update(float delta){
        this.camera.update(delta);
        if(isKeyPressed(GLFW_KEY_ESCAPE))
            this.window.shouldClose();
    }

    private void Render(){
        // Render Light Source
        this.shaderLight.bind();
        this.shaderLight.setMat4(this.camera.getCameraMatrix(), "Camera");
        this.shaderLight.setModel(this.transformLight);
        this.shaderLight.setVec3(this.light.getColor().toVec3(), "Color");
        this.mesh.render();
        this.shaderLight.unbind();

        // Render Object
        this.shaderMesh.bind();
        this.shaderMesh.setMat4(this.camera.getCameraMatrix(), "Camera");
        this.shaderMesh.setModel(this.transformMesh);
        this.shaderMesh.setMaterial(this.material);
        this.shaderMesh.setLight(this.light);
        this.shaderMesh.setVec3(this.camera.getCameraPosition(), "CameraPos");
        this.mesh.render();
        this.shaderMesh.unbind();


        window.display();
    }

    private void ClearMemory(){
        this.mesh.clearMemory();
        this.shaderLight.clearMemory();
        this.shaderMesh.clearMemory();
        this.window.clearMemory();
    }

    public MaterialTest(){
        Init();

        double lastTime = glfwGetTime();
        while(this.window.isOpen()){
            double currentTime = glfwGetTime();
            double delta = currentTime - lastTime;
            if(delta > 1.0/60){
                lastTime = currentTime;
                Update((float)delta);
            }
            Render();
        }
        ClearMemory();
    }

    public static void main(String[] args) {
        new MaterialTest();
    }
}
