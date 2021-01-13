package pl.piotrkniemczuk.ball.engine;

import pl.piotrkniemczuk.ball.graphics.Graphic;

public interface State {

    void Init(Window window, Input input, GameEngine ge);

    void Update(Window window, Input input, GameEngine ge, float delta);

    void Render(Window window, Input input, GameEngine ge);
}
