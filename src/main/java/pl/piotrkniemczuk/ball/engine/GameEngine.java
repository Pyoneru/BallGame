package pl.piotrkniemczuk.ball.engine;

import pl.piotrkniemczuk.ball.assets.Assets;
import pl.piotrkniemczuk.ball.assets.Mesh;
import pl.piotrkniemczuk.ball.graphics.*;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class GameEngine implements Dispose {

    private final Window window;
    private Graphic graphic;
    private int fps;
    private State state;
    private List<Collider> colliders;

    public GameEngine(GameSettings settings, State state) {
        this.window = new Window(
                settings.getWindowSize().x,
                settings.getWindowSize().y,
                settings.getTitle(),
                settings.isFullscreen());
        Input.init(window);
        this.fps = settings.getFPS();
        AssetsInitialize(settings);
        GraphicInitialize(settings);
        this.colliders = new ArrayList<>();
        setState(state);
    }

    private void GraphicInitialize(GameSettings settings) {
        // Create graphic instance, first invoked.
        this.graphic = Graphic.getInstance();

        // Enable Light
        this.graphic.setEnableLight(settings.isEnableLight());

        // Create Light Object and add to Graphic
        Light light = new Light(
                settings.getLightPos(),
                settings.getLightColor(),
                settings.getLightAmbient(),
                settings.getLightDiffuse(),
                settings.getLightSpecular());
        this.graphic.setLight(light);

        // set Light Scale(for object)
        this.graphic.setLightScale(settings.getLightScale());

        // Create Camera
        Camera camera = null;
        switch(settings.getCameraType()){
            case "free":
                camera = new FreeCamera(settings.getCameraPos(), window);
                break;

            case "player":
                camera = new PlayerCamera(settings.getCameraPos(), window);
                break;

            default:
                camera = new FreeCamera(settings.getCameraPos(), window);
                break;
        }
        this.graphic.setCamera(camera);
    }

    private void AssetsInitialize(GameSettings settings) {
        Assets assets = Assets.getInstance();
        settings.getShaders().forEach(assets::addShader);
        settings.getModels().forEach(assets::addModel);
        settings.getTextures().forEach(assets::addTexture);

        assets.addShader("Object");
        assets.addShader("Light");
        assets.addModel("cube");
        assets.addModel("plane", Mesh.createPlane());
    }

    public void start() {
        window.show();
        double lastTime = glfwGetTime();
        while (window.isOpen()) {
            double currentTime = glfwGetTime();
            double delta = currentTime - lastTime;
            if (delta > 1.0 / fps) {
                lastTime = currentTime;
                graphic.update((float) delta);
                state.Update(window, this, (float) delta);
                CollisionDetection();
            }
            graphic.render();
            state.Render(window, this);
            window.display();
        }
        clearMemory();
    }

    // ToDo: Rework Collision Detection
    private void CollisionDetection() {
        for (Collider collider : colliders) {
            for (Collider collider1 : colliders) {
                if (collider != collider1) {
                    if (collider.isCollision(collider1)) {
                        if (!collider.getEnteredCollider().contains(collider1)) {
                            collider.enter(collider1);
                            collider.getEnteredCollider().add(collider1);
                        }
                        collider.collision(collider1);
                    } else if (collider.getEnteredCollider().contains(collider1)) {
                        collider.getEnteredCollider().remove(collider1);
                        collider.exit(collider1);
                    }
                }
            }
        }
    }

    public List<Collider> getColliders() {
        return colliders;
    }

    public void setColliders(List<Collider> colliders) {
        this.colliders = colliders;
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public Window getWindow() {
        return window;
    }

    public void setState(State state) {
        this.state = state;
        this.state.Init(window, this);
    }

    @Override
    public void clearMemory() {
        Assets.getInstance().clearMemory();
        this.window.clearMemory();
        this.state.clearMemory();
    }
}
