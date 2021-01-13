package pl.piotrkniemczuk.ball.test;
import org.lwjgl.util.par.*;
import pl.piotrkniemczuk.ball.assets.Mesh;
import pl.piotrkniemczuk.ball.assets.Shader;
import pl.piotrkniemczuk.ball.assets.Texture;
import pl.piotrkniemczuk.ball.engine.Input;
import pl.piotrkniemczuk.ball.engine.Transform;
import pl.piotrkniemczuk.ball.engine.Window;
import pl.piotrkniemczuk.ball.graphics.Camera;
import pl.piotrkniemczuk.ball.graphics.Color;
import pl.piotrkniemczuk.ball.graphics.FreeCamera;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.util.par.ParShapes.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.glfw.GLFW.*;

/**
 * Failed test Par Shapes
 */
public class PerShapesLibTest {

    private Window window;
    private Input input;
    private Mesh mesh;
    private Shader shader;
    private Texture texture;
    private Color color;
    private Camera camera;

    private Transform transform;

    private void Init(){
        this.window = new Window(1280, 720, "Par Shapes Lib Test");
        this.input = new Input(window);
        this.camera = new FreeCamera(input, window);
        ((FreeCamera)camera).velocity = 5.0f;
        this.shader = Shader.create()
                .attachShader("./Assets/Shaders/test/CameraVertex.glsl", GL_VERTEX_SHADER)
                .attachShader("./Assets/Shaders/test/CameraFragment.glsl", GL_FRAGMENT_SHADER)
                .link();
        this.texture = new Texture("./Assets/Textures/image.png");
        this.color = Color.Magenta;
        this.transform = new Transform();
        InitMesh();
        glfwShowWindow(window.getWindow());
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void InitMesh(){
        ParShapesMesh par = par_shapes_create_subdivided_sphere(0);
        FloatBuffer vertices = par.points(par.sizeof());
        FloatBuffer texture = par.tcoords(par.sizeof());
        FloatBuffer normal = par.normals(par.sizeof());
        IntBuffer indices = par.triangles(par.sizeof());
        int count = par.sizeof();
        System.out.println(count);
        this.mesh = new Mesh(vertices, texture, normal, indices, count);
        transform.position.z -= 10.0f;
        transform.scale.mul(10.0f);
//        par_shapes_free_mesh(par);
    }

    private void Update(float delta){
        camera.update(delta);
        if(input.isKeyPressed(GLFW_KEY_ESCAPE))
            glfwSetWindowShouldClose(window.getWindow(), true);
    }

    private void Render(){
        shader.bind();
        texture.bind();
        shader.setVec3(color.toVec3(), "Color");
        shader.setModel(transform);
        shader.setMat4(camera.getCameraMatrix(), "Camera");
        mesh.render();
        texture.unbind();
        shader.unbind();

        window.display();
    }

    private void ClearMemory(){
        texture.clearMemory();
        shader.clearMemory();
        mesh.clearMemory();
        window.clearMemory();
    }

    public PerShapesLibTest(){
      Init();

      double lastTime = glfwGetTime();
      while(window.isOpen()){
          double currentTime = glfwGetTime();
          double delta = currentTime - lastTime;
          lastTime = currentTime;
          if(delta > 1.0/60){
              Update((float)delta);
          }

          Render();
      }

      ClearMemory();
    }

    public static void main(String[] args) {
        new PerShapesLibTest();
    }
}
