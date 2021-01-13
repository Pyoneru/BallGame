package pl.piotrkniemczuk.ball.engine;

import pl.piotrkniemczuk.ball.assets.Assets;
import pl.piotrkniemczuk.ball.assets.Mesh;
import pl.piotrkniemczuk.ball.graphics.*;
import pl.piotrkniemczuk.ball.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class GameEngine {

    private Window window;
    private Input input;
    private Graphic graphic;
    private int fps;
    private State state;
    private List<Collider> colliders;

    public GameEngine(GameSettings settings, State state) {
        long win = Utils.getTime();
        this.window = new Window(
                settings.getWindowSize().x,
                settings.getWindowSize().y,
                settings.getTitle(),
                settings.isFullscreen());
        System.out.println("Create window: " + (Utils.getTime() - win));
        this.input = new Input(window);
        this.fps = settings.getFPS();
        AssetsInitialize(settings);
        GraphicInitialize(settings);
        this.colliders = new ArrayList<>();
        setState(state);
    }

    private void GraphicInitialize(GameSettings settings) {
        long graphicTime = Utils.getTime();
        this.graphic = Graphic.getInstance();
        System.out.println("Create Graphic: " + (Utils.getTime() - graphicTime));
        this.graphic.setEnableLight(settings.isEnableLight());
        long lightTime = Utils.getTime();
        Light light = new Light(
                settings.getLightPos(),
                settings.getLightColor(),
                settings.getLightAmbient(),
                settings.getLightDiffuse(),
                settings.getLightSpecular());
        System.out.println("Create Light: " + (Utils.getTime() - lightTime));
        this.graphic.setLight(light);
        this.graphic.setLightScale(settings.getLightScale());

        Camera camera = null;
        if (settings.getCameraType().equals("free")) {
            long cameraTime = Utils.getTime();
            camera = new FreeCamera(settings.getCameraPos(), input, window);
            System.out.println("Create Camera: " + (Utils.getTime() - cameraTime));
        } else if (settings.getCameraType().equals("player")) {
            long cameraTime = Utils.getTime();
            camera = new PlayerCamera(settings.getCameraPos(), input, window);
            System.out.println("Create Camera: " + (Utils.getTime() - cameraTime));
        } else {
            long cameraTime = Utils.getTime();
            camera = new FreeCamera(settings.getCameraPos(), input, window);
            System.out.println("Create Camera: " + (Utils.getTime() - cameraTime));
        }
        this.graphic.setCamera(camera);
    }

    private void AssetsInitialize(GameSettings settings) {
        long assetsTime = Utils.getTime();
        Assets assets = Assets.getInstance();
        System.out.println("Create Assets: " + (Utils.getTime() - assetsTime));
        settings.getShaders().forEach(assets::addShader);
        settings.getModels().forEach(assets::addModel);
        for (GameSettings.TexturePair texturePair : settings.getTextures()) {
            assets.addTexture(texturePair.texture, texturePair.type);
        }
        assets.addShader("Object");
        assets.addShader("Light");
        assets.addModel("cube");
        float[] vertices = {
                -1.0f, 1.0f, 0.0f,
                -1.0f, -1.0f, 0.0f,
                1.0f, -1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                -1.0f, 1.0f, 0.0f
        };

        float[] texture = {
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f
        };

        float[] normal = {
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f
        };

        int[] indices = {
                0, 1, 2,
                2, 3, 0
        };

        long a = Utils.getTime();
        assets.addModel("plane", new Mesh(vertices, texture, normal, indices));
        System.out.println("Mesh: " + (Utils.getTime() - a));
    }

    public void start() {
        GameAnalyze ga = GameAnalyze.getInstance();
        window.show();
        double lastTime = glfwGetTime();
        double nextCounter = lastTime + 1.0;
        int fpsCounter = 0;
        while (window.isOpen()) {
            double currentTime = glfwGetTime();
            double delta = currentTime - lastTime;
            if (delta > 1.0 / fps) {
                lastTime = currentTime;
                graphic.update((float) delta);
                state.Update(window, input, this, (float) delta);
                CollisionDetection();
            }
            graphic.render();
            state.Render(window, input, this);
            ga.timeMeter += 1;
            window.display();
            fpsCounter++;
            if(currentTime >= nextCounter){
                ga.avgFPS = ga.avgFPS + fpsCounter;
                ga.avg++;
                if(fpsCounter < ga.minFPS) ga.minFPS = fpsCounter;
                if(fpsCounter > ga.maxFPS) ga.maxFPS = fpsCounter;
                fpsCounter = 0;
                nextCounter = currentTime + 1.0;
                ga.update();
            }
        }
        clearMemory();
    }

    private void CollisionDetection() {
        for (Collider collider : colliders) {
            for (Collider collider1 : colliders) {
                if (collider != collider1) {
                    if (
                                    collider.getUp() >= collider1.getDown() && collider.getDown() <= collider1.getUp() &&
                                    collider.getLeft() <= collider1.getRight() && collider.getRight() >= collider1.getLeft() &&
                                    collider.getForward() >= collider1.getBack() && collider.getBack() <= collider1.getForward()
                    ) {
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
        this.state.Init(window, input, this);
    }

    public void clearMemory() {
        Assets.getInstance().clearMemory();
        this.window.clearMemory();
    }
}
