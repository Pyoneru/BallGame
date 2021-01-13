package pl.piotrkniemczuk.ball;

import pl.piotrkniemczuk.ball.engine.GameEngine;
import pl.piotrkniemczuk.ball.engine.GameSettings;
import pl.piotrkniemczuk.ball.states.GameState;
import pl.piotrkniemczuk.ball.utils.Utils;

public class Main {

    private static final String settingsPath = "./Assets/settings.cfg";

    public static void main(String[] args) {
        long timeSettings = Utils.getTime();
        GameSettings settings = new GameSettings(settingsPath);
        System.out.println("Create GameSettings: " + (Utils.getTime() - timeSettings));
        long timeEngine = Utils.getTime();
        GameEngine engine = new GameEngine(settings, new GameState());
        System.out.println("Create GameEngine: " + (Utils.getTime() - timeEngine));
        engine.start();
    }
}
