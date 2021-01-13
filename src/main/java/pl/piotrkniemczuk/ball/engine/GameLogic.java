package pl.piotrkniemczuk.ball.engine;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4i;
import pl.piotrkniemczuk.ball.assets.Assets;
import pl.piotrkniemczuk.ball.assets.Material;
import pl.piotrkniemczuk.ball.assets.Mesh;
import pl.piotrkniemczuk.ball.assets.Texture;
import pl.piotrkniemczuk.ball.graphics.Color;
import pl.piotrkniemczuk.ball.graphics.FreeCamera;
import pl.piotrkniemczuk.ball.graphics.Graphic;
import pl.piotrkniemczuk.ball.objects.Board;
import pl.piotrkniemczuk.ball.objects.GameObject;
import pl.piotrkniemczuk.ball.objects.Point;
import pl.piotrkniemczuk.ball.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class GameLogic {

    private int playerPoints;

    private Board board;
    private Vector3f cameraPosition;
    private List<Point> points;
    private Assets assets;
    private Graphic graphic;
    private GameObject[] gameObject;
    private Texture texture;

    private GameEngine ge;
    private Vector4i boardSize;
    private boolean randomCameraPosition;
    private float pointsSeed;
    private float gravity;

    private int lvl;

    private GameLogic(GameEngine ge,
                      Vector4i boardSize,
                      boolean randomCameraPosition,
                      float pointsSeed,
                      float gravity){
        lvl = 1;
        this.ge = ge;
        this.boardSize = boardSize;
        this.randomCameraPosition = randomCameraPosition;
        this.pointsSeed = pointsSeed;
        this.gravity = gravity;
        this.assets = Assets.getInstance();
        this.graphic = Graphic.getInstance();
        assets.addTexture("image", Assets.ImageType.PNG);
        texture = assets.getTexture("image");
        Init();
    }

    private void Init() {
//        int x = Utils.random.nextInt(boardSize.z - boardSize.x) + boardSize.x;
//        int y = Utils.random.nextInt(boardSize.w - boardSize.y) + boardSize.y;
        Level l = getLevel(lvl);
        System.out.println(l);
        InitializeBoard(ge, new Vector2i(l.x, l.y));
        if(randomCameraPosition)
            InitializeCameraPos(l.x, l.y);
        InitializePoints(l.point, l.x, l.y);
        playerPoints = 0;
        graphic.getCamera().scale.set(new Vector3f(0.05f, 1.0f, 0.05f));
        ge.getColliders().add(graphic.getCamera());
        InitializeGameObject(l.x, l.y);
    }

    private void InitializeBoard(GameEngine ge, Vector2i size){
        this.board = new Board(size, new Vector3f(), 15);
        this.board.setColliders(ge);
    }

    private void InitializeCameraPos(int x, int y){
        int posX = Utils.random.nextInt(x);
        int posZ = Utils.random.nextInt(y);
        this.cameraPosition = new Vector3f(posX * 2.0f, 10.0f, posZ * 2.0f);
        graphic.getCamera().position.set(cameraPosition);
    }

    private void InitializePoints(float seed, int x, int y){
        this.points = new ArrayList<>();
        int size = x * y;
        if(seed < 0) seed = 0.0f;
        if(seed > 1.0f) seed = 1.0f;
        int seedValue = (int)(size * seed);
        List<Vector2f> randedPosition = new ArrayList<>();
        for(int i = 0; i < seedValue; i++) {
            Point point = new Point(generateColor());
            int posX = Utils.random.nextInt(x);
            int posZ = Utils.random.nextInt(y);
            while(!randedPosition.isEmpty() && randedPosition.contains(new Vector2f(posX, posZ))){
                posX = Utils.random.nextInt(x);
                posZ = Utils.random.nextInt(y);
            }
            randedPosition.add(new Vector2f(posX, posZ));
            point.position.set(posX * 2.0f, 1.0f, posZ * 2.0f);
            points.add(point);
        }

        points.forEach(ge.getColliders()::add);
    }

    private void InitializeGameObject(int x, int y){
        gameObject = new GameObject[2];
        Mesh cube = assets.getModel("cube");
        gameObject[0] = new GameObject(cube, Material.Gold);
        gameObject[0].position.set((x*2)/2, 30.0f, (y*2)/2);
        gameObject[0].scale.set(10.0f);

        gameObject[1] = new GameObject(cube, Material.Gold);
        gameObject[1].position.set(-(x*2)/2, 30.0f, -(y*2)/2);
        gameObject[1].scale.set(10.0f);
        gameObject[1].setTexture(texture);
    }

    private Color generateColor(){
        int color = Utils.random.nextInt(10);
        if(color == 0) return Color.Red;
        if(color == 1) return Color.Green;
        if(color == 2) return Color.Blue;
        if(color == 3) return Color.Gold;
        if(color == 4) return Color.Orange;
        if(color == 5) return Color.Cyan;
        if(color == 6) return Color.Yellow;
        if(color == 7) return Color.Pink;
        if(color == 8) return Color.Maroon;
        if(color == 9) return Color.Magenta;
        else return Color.White;
    }

    public void update(float delta){
        for(int i = 0; i < points.size(); i++){
            Point point = points.get(i);
            point.update(delta);
            if(point.isCollected()){
                ge.getColliders().remove(point);
                points.remove(i--);
            }
        }

        if(points.isEmpty()) Win();

        for(int i = 0; i < gameObject.length; i++) {
            gameObject[i].rotation.z = (float) Math.toRadians(glfwGetTime() * 15 * 0.5f);
            gameObject[i].rotation.y = (float) Math.toRadians(glfwGetTime() * 15);
            gameObject[i].rotation.x = (float) Math.toRadians(glfwGetTime() * 15 * 0.8f);
        }

        graphic.getCamera().position.y -= gravity;
        if(graphic.getCamera().position.y < -10.0f) Lose();
    }

    private void Win(){
        lvl++;
        if(getLevel(lvl) != null)
            Lose();
        else
            ge.getWindow().shouldClose();
    }

    private void Lose(){
        board = null;
        points = null;
        ge.getColliders().clear();
        System.gc();
        Init();
    }

    public void render(){
        for(int i = 0; i < points.size(); i++){
            points.get(i).render();
        }
        for(GameObject go : gameObject)
            graphic.render(go);
        board.render();
    }

    private Level getLevel(int level){
        Level lvl = null;
        switch (level){
            case 1:
                lvl = new Level(5, 5, (float)5/(5*5));
                break;
            case 2:
                lvl = new Level(10, 10, (float)5/(10*10));
                break;
            case 3:
                lvl = new Level(15, 15, (float)5/(15*15));
                break;
            case 4:
                lvl = new Level(20, 20, (float)5/(20*20));
                break;
            case 5:
                lvl = new Level(25, 25, (float)5/(20*20));
                break;
            case 6:
                lvl = new Level(30, 30, (float)5/(30*30));
                break;
            case 7:
                lvl = new Level(50, 50, (float)5/(50*50));
                break;
        }
        return lvl;
    }

    public static class Builder{
        private GameEngine ge;
        private Vector4i boardSize;
        private boolean randomCameraPosition;
        private float pointsSeed;
        private float gravity;
        public Builder(GameEngine ge){
            this.ge = ge;
            this.boardSize = new Vector4i(8,8, 16, 16);
            this.randomCameraPosition = true;
            this.pointsSeed = 0.35f;
            this.gravity = 0.1f;
        }

        public Builder boardSize(Vector4i boardSize){
            this.boardSize = boardSize;
            return this;
        }

        public Builder randomCameraPosition(boolean enabled){
            this.randomCameraPosition = enabled;
            return this;
        }

        public Builder pointsSeed(float seed){
            this.pointsSeed = seed;
            return this;
        }

        public Builder gravity(float gravity){
            this.gravity = gravity;
            return this;
        }

        public GameLogic build(){
            return new GameLogic(ge, boardSize, randomCameraPosition, pointsSeed, gravity);
        }
    }

    private class Level{
        public int x;
        public int y;
        public float point;

        public Level(int x, int y, float point) {
            this.x = x;
            this.y = y;
            this.point = point;
        }

        @Override
        public String toString() {
            return "Level{" +
                    "x=" + x +
                    ", y=" + y +
                    ", point=" + point +
                    '}';
        }
    }
}
