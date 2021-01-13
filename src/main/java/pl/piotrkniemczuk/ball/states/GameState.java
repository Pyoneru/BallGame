package pl.piotrkniemczuk.ball.states;

import pl.piotrkniemczuk.ball.engine.*;
import pl.piotrkniemczuk.ball.graphics.*;


import static org.lwjgl.glfw.GLFW.*;

public class GameState implements State {

    private Graphic graphic;
    private AnalyzeGameLogic gameLogic;

    @Override
    public void Init(Window window, Input input, GameEngine ge) {
        graphic = Graphic.getInstance();
        gameLogic = new AnalyzeGameLogic(ge);
    }

    @Override
    public void Update(Window window, Input input, GameEngine ge, float delta) {
        if(input.isKeyPressed(GLFW_KEY_ESCAPE))
            window.shouldClose();
        UpdateLightColor(input);
        gameLogic.update(delta, input);
    }

    private void UpdateLightColor(Input input) {
        if(input.isKeyPressed(GLFW_KEY_R))
            graphic.getLight().setColor(Color.Red);
        if(input.isKeyPressed(GLFW_KEY_G))
            graphic.getLight().setColor(Color.Green);
        if(input.isKeyPressed(GLFW_KEY_B))
            graphic.getLight().setColor(Color.Blue);
        if(input.isKeyPressed(GLFW_KEY_P))
            graphic.getLight().setColor(Color.White);

        if(input.isKeyPressed(GLFW_KEY_1))
            graphic.getLight().setColor(Color.Orange);
        if(input.isKeyPressed(GLFW_KEY_2))
            graphic.getLight().setColor(Color.Gold);
        if(input.isKeyPressed(GLFW_KEY_3))
            graphic.getLight().setColor(Color.Magenta);
        if(input.isKeyPressed(GLFW_KEY_4))
            graphic.getLight().setColor(Color.Yellow);
        if(input.isKeyPressed(GLFW_KEY_5))
            graphic.getLight().setColor(Color.Pink);
        if(input.isKeyPressed(GLFW_KEY_6))
            graphic.getLight().setColor(Color.Cyan);
        if(input.isKeyPressed(GLFW_KEY_7))
            graphic.getLight().setColor(Color.Maroon);
        if(input.isKeyPressed(GLFW_KEY_8))
            graphic.getLight().setColor(new Color(128, 128, 128));
        if(input.isKeyPressed(GLFW_KEY_9))
            graphic.getLight().setColor(Color.Black);
    }

    @Override
    public void Render(Window window, Input input, GameEngine ge) {
        gameLogic.render();
    }
}
