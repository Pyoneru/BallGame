package pl.piotrkniemczuk.ball.engine;

import pl.piotrkniemczuk.ball.graphics.Graphic;

public interface State extends Dispose {

    void Init(Window window, GameEngine ge);

    void Update(Window window, GameEngine ge, float delta);

    void Render(Window window, GameEngine ge);
}
