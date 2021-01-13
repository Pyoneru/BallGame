package pl.piotrkniemczuk.ball.test;

import org.joml.Vector3f;
import pl.piotrkniemczuk.ball.assets.Mesh;
import pl.piotrkniemczuk.ball.assets.Shader;
import pl.piotrkniemczuk.ball.engine.Input;
import pl.piotrkniemczuk.ball.engine.Transform;
import pl.piotrkniemczuk.ball.engine.Window;
import pl.piotrkniemczuk.ball.graphics.Color;
import pl.piotrkniemczuk.ball.graphics.FreeCamera;
import pl.piotrkniemczuk.ball.graphics.Light;
import pl.piotrkniemczuk.ball.utils.OBJLoader;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.glfw.GLFW.*;

public class LightTest {

    private Window window;
    private Input input;
    private FreeCamera camera;

    private Mesh mesh;
    private Shader shaderLight;
    private Shader shader;

    private Transform transform;
    private Color color;

    private Light light;
    private Transform lightTransform;

    private void Init(){
        this.window = new Window(1280, 720, "Light Test");
        this.input = new Input(this.window);
        this.camera = new FreeCamera(this.input, this.window);
        InitAssets();
        InitProperties();
        this.light = new Light(new Vector3f(10.0f, 12.0f, -5.0f));
        this.lightTransform.position = light.getPosition();

        window.show();
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    private void InitAssets(){
        this.mesh = OBJLoader.loadModel("./Assets/Models/cube.obj");
       this.shaderLight = Shader.create()
               .attachShader("./Assets/Shaders/test/LightSourceVertex.glsl", GL_VERTEX_SHADER)
               .attachShader("./Assets/Shaders/test/LightSourceFragment.glsl", GL_FRAGMENT_SHADER)
               .link();
       this.shader = Shader.create()
               .attachShader("./Assets/Shaders/test/LightObjectVertex.glsl", GL_VERTEX_SHADER)
               .attachShader("./Assets/Shaders/test/LightObjectFragment.glsl", GL_FRAGMENT_SHADER)
               .link();
    }

    private void InitProperties(){
        this.transform = new Transform();
        this.transform.scale.mul(5.0f);
        this.transform.position.z -= 10.0f;

        this.color = Color.Orange;

        this.lightTransform = new Transform();
        this.lightTransform.scale.mul(0.5f);
    }

    public void Update(float delta){
        this.camera.update(delta);
        if(this.input.isKeyPressed(GLFW_KEY_ESCAPE))
            this.window.shouldClose();
    }

    public void Render(){
        //Render Light
        shaderLight.bind();
        shaderLight.setMat4(camera.getCameraMatrix(), "Camera");
        shaderLight.setModel(lightTransform);
        shaderLight.setVec3(light.getColor().toVec3(), "Color");
        mesh.render();
        shaderLight.unbind();

        //Object Light
        shader.bind();
        shader.setMat4(camera.getCameraMatrix(), "Camera");
        shader.setModel(transform);
        shader.setVec3(color.toVec3(), "Color");
        shader.setVec3(light.getColor().toVec3(), "LightColor");
        shader.setVec3(light.getPosition(), "LightPos");
        shader.setFloat(light.getAmbientStrength(), "AmbientStrength");
        shader.setFloat(light.getSpecular(), "SpecularStrength");
        shader.setVec3(camera.getCameraPosition(), "CameraPos");
        mesh.render();
        shader.unbind();

        window.display();
    }

    public void ClearMemory(){
        this.mesh.clearMemory();
        this.shader.clearMemory();
        this.window.clearMemory();
    }

    public LightTest(){
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
        new LightTest();
    }
}
