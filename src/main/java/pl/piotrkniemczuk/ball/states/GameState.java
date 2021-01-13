package pl.piotrkniemczuk.ball.states;

import org.joml.Vector2i;
import org.joml.Vector3f;
import pl.piotrkniemczuk.ball.engine.*;
import pl.piotrkniemczuk.ball.graphics.*;
import pl.piotrkniemczuk.ball.objects.Board;


import static org.lwjgl.glfw.GLFW.*;

public class GameState implements State {

    private Graphic graphic;
    private Board board;
    private Camera camera;
    private float gravity = 0.9f;

    @Override
    public void Init(Window window, Input input, GameEngine ge) {
        graphic = Graphic.getInstance();
        board = new Board(new Vector2i(10, 10), new Vector3f(0,0,0), 10);
        board.setColliders(ge);
        camera = graphic.getCamera();
        ge.getColliders().add(camera);
    }

    @Override
    public void Update(Window window, Input input, GameEngine ge, float delta) {
        if(input.isKeyPressed(GLFW_KEY_ESCAPE))
            window.shouldClose();

        camera.position.y -= gravity;
    }

    @Override
    public void Render(Window window, Input input, GameEngine ge) {
        board.render();
    }
}
