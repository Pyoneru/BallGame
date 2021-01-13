package pl.piotrkniemczuk.ball.test;

import org.joml.Vector3f;
import pl.piotrkniemczuk.ball.assets.Mesh;
import pl.piotrkniemczuk.ball.assets.Shader;
import pl.piotrkniemczuk.ball.assets.Texture;
import pl.piotrkniemczuk.ball.engine.Input;
import pl.piotrkniemczuk.ball.engine.Transform;
import pl.piotrkniemczuk.ball.engine.Window;
import pl.piotrkniemczuk.ball.graphics.Color;
import pl.piotrkniemczuk.ball.graphics.FreeCamera;
import pl.piotrkniemczuk.ball.utils.OBJLoader;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.glfw.GLFW.*;
import static pl.piotrkniemczuk.ball.engine.Input.*;

public class ObjLoaderTest {

    private Window window;
    private FreeCamera camera;

    private Mesh mesh;
    private Shader shader;
    private Texture texture;


    private Transform transform;
    private Color color;

    private void Init(){
        this.window = new Window(1280, 720, "Obj Loader Test");
        this.camera = new FreeCamera(this.window);
        this.camera.velocity = 4.0f;
        InitAssets();
        this.window.show();
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void InitAssets(){
        this.mesh = OBJLoader.loadModel("./Assets/Models/cube.obj");
        if(this.mesh == null) {
            try {
                throw new Exception("Mesh is null");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.shader = Shader.create()
                .attachShader("./Assets/Shaders/test/ObjVertex.glsl", GL_VERTEX_SHADER)
                .attachShader("./Assets/Shaders/test/ObjFragment.glsl", GL_FRAGMENT_SHADER)
                .link();
        this.texture = new Texture("./Assets/Textures/image.png");

        this.transform = new Transform(
                new Vector3f(0.0f, 0.0f, -5.0f),
                new Vector3f(),
                new Vector3f(4.0f, 4.0f, 4.0f)
        );

        this.color = Color.Gold;
    }

    public void Update(float delta){
        camera.update(delta);
        if(isKeyPressed(GLFW_KEY_ESCAPE))
            this.window.shouldClose();
    }

    public void Render(){
        this.shader.bind();
        this.texture.bind();;
        this.shader.setModel(this.transform);
        this.shader.setMat4(this.camera.getCameraMatrix(), "Camera");
        this.shader.setVec3(this.color.toVec3(), "Color");
        this.mesh.render();
        this.texture.unbind();
        this.shader.unbind();

        this.window.display();
    }

    public void ClearMemory(){
        this.mesh.clearMemory();
        this.shader.clearMemory();
        this.window.clearMemory();
    }

    public ObjLoaderTest(){
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
        new ObjLoaderTest();
    }
}
