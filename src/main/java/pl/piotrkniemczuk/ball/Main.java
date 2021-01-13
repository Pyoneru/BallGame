package pl.piotrkniemczuk.ball;

import pl.piotrkniemczuk.ball.engine.GameEngine;
import pl.piotrkniemczuk.ball.engine.GameSettings;
import pl.piotrkniemczuk.ball.states.GameState;
import pl.piotrkniemczuk.ball.utils.Utils;

public class Main {

    private static final String settingsPath = "./Assets/settings.cfg";

    public static void main(String[] args) {
        GameSettings settings = new GameSettings(settingsPath);
        GameEngine engine = new GameEngine(settings, new GameState());
        engine.start();
    }
}
