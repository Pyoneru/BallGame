package pl.piotrkniemczuk.ball.engine;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import pl.piotrkniemczuk.ball.assets.Assets;
import pl.piotrkniemczuk.ball.graphics.Color;
import pl.piotrkniemczuk.ball.graphics.Graphic;
import pl.piotrkniemczuk.ball.objects.Board;
import pl.piotrkniemczuk.ball.objects.GameObject;
import pl.piotrkniemczuk.ball.objects.Point;
import pl.piotrkniemczuk.ball.utils.Utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_N;

public class AnalyzeGameLogic {

    private Board board;
    private List<Point> points;
    private Graphic graphic;

    private GameEngine ge;
    private float gravity;

    private GameAnalyze ga;
    private Level l;

    public AnalyzeGameLogic(GameEngine ge){
        this. ge = ge;
        this.gravity = 0.1f;
        this.graphic = Graphic.getInstance();
        this.ga = GameAnalyze.getInstance();
        Init();
    }

    public void Init(){
        ga.timeLight = 0;
        ga.timeAvgFields = 0;
        ga.timeAllFields = 0;
        ga.timeAvgBorders = 0;
        ga.timeAllBorders = 0;
        ga.timeMeter = 0;
        System.out.println("====Level " + ga.getLvl() + "====");
        l = getLevel(ga.getLvl());
        // Initialize Board
        long timeBoard = Utils.getTime();
        board = new Board(new Vector2i(l.x, l.y), new Vector3f(), 5);
        System.out.println("Create Board: " + (Utils.getTime() - timeBoard));
        board.setColliders(ge);
        // Initialize Camera Position
        RandomCameraPos();
        // Initialize Points
        InitializePoints(l.seed, l.x, l.y);
        ge.getColliders().add(graphic.getCamera());

    }

    private void RandomCameraPos() {
        int posX = Utils.random.nextInt(l.x);
        int posZ = Utils.random.nextInt(l.y);
        Vector3f cameraPos = new Vector3f(posX * 2.0f, 10.0f, posZ * 2.0f);
        graphic.getCamera().position.set(cameraPos);
    }

    private void InitializePoints(float seed, int x, int y){
        this.points = new ArrayList<>();
        int size = x * y;
        if(seed < 0) seed = 0.0f;
        if(seed > 1.0f) seed = 1.0f;
        int seedValue = (int)(size * seed);
        List<Vector2f> randedPosition = new ArrayList<>();
        long avg = 0;
        for(int i = 0; i < seedValue; i++) {
            long time = Utils.getTime();
            Point point = new Point(generateColor());
            long res = Utils.getTime() - time;
            avg += res;
//            System.out.println("Create Point: " + res);
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
        System.out.println("Point avg: " + (avg/seedValue));
        System.out.println("Point All: " + avg);


        points.forEach(ge.getColliders()::add);
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

    private Level getLevel(int level){
        Level lvl = null;
        switch (level){
            case 1:
                lvl = new Level(5, 5, (float)5/(5*5));
                break;
            case 2:
                lvl = new Level(10, 10, (float)10/(10*10));
                break;
            case 3:
                lvl = new Level(10, 10, (float)50/(10*10));
                break;
            case 4:
                lvl = new Level(20, 20, (float)20/(20*20));
                break;
            case 5:
                lvl = new Level(20, 20, (float)100/(20*20));
                break;
            case 6:
                lvl = new Level(30, 30, (float)30/(30*30));
                break;
            case 7:
                lvl = new Level(30, 30, (float)150/(30*30));
                break;
            case 8:
                lvl = new Level(40, 40, (float)40/(40*40));
                break;
            case 9:
                lvl = new Level(40, 40, (float)200/(40*40));
                break;
        }
        return lvl;
    }

    public void update(float delta, Input input){
        for(int i = 0; i < points.size(); i++){
            Point point = points.get(i);
            point.update(delta);
            if(point.isCollected()){
                ge.getColliders().remove(point);
                points.remove(i--);
            }
        }

        if(input.isKeyPressed(GLFW_KEY_N)) Win();

        if(points.isEmpty()) Win();

        graphic.getCamera().position.y -= gravity;

        if(graphic.getCamera().position.y < -10.0f) RandomCameraPos();
    }

    public void render(){
        for(int i = 0; i < points.size(); i++){
            points.get(i).render();
        }
        board.render();
    }

    private void Win(){
        System.out.println("Light: " + ga.timeLight/ga.timeMeter);
        System.out.println("Field Avg: " + ga.timeAvgFields/ga.timeMeter);
        System.out.println("Field All: " + ga.timeAllFields/ga.timeMeter);
        System.out.println("Border Avg:  " + ga.timeAvgBorders/ga.timeMeter);
        System.out.println("Border All:  " + ga.timeAllBorders/ga.timeMeter);
        ga.nextLvl();
        if(getLevel(ga.getLvl()) != null)
            Lose();
        else {
            ge.getWindow().shouldClose();
            ga.save();
        }
    }

    private void Lose(){
        board = null;
        points = null;
        ge.getColliders().clear();
        System.gc();
        Init();
    }

    private class Level{
        public int x;
        public int y;
        public float seed;

        public Level(int x, int y, float seed) {
            this.x = x;
            this.y = y;
            this.seed = seed;
        }

        @Override
        public String toString() {
            return "Level{" +
                    "x=" + x +
                    ", y=" + y +
                    ", seed=" + seed +
                    '}';
        }
    }
}
