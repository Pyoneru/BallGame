package pl.piotrkniemczuk.ball.objects;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import pl.piotrkniemczuk.ball.assets.Assets;
import pl.piotrkniemczuk.ball.assets.Mesh;
import pl.piotrkniemczuk.ball.assets.Shader;
import pl.piotrkniemczuk.ball.engine.Collider;
import pl.piotrkniemczuk.ball.engine.GameEngine;
import pl.piotrkniemczuk.ball.engine.Transform;
import pl.piotrkniemczuk.ball.graphics.Color;
import pl.piotrkniemczuk.ball.graphics.Graphic;
import pl.piotrkniemczuk.ball.utils.Utils;

import static org.lwjgl.opengl.GL33.*;

public class Board {

    private final Mesh mesh;
    private final float borderBreak;
    private final Shader shader;
    private Transform transform;
    private final Field[][] fields;
    private final Border[][] borders;
    private final Vector2i size;
    private final Graphic graphic;
    private final int step;

    public Board(Vector2i size, Vector3f position, int step){
        transform = new Transform();
        this.transform.position.set(position);
        this.size = size;
        this.step = step;

        graphic = Graphic.getInstance();
        Assets assets = Assets.getInstance();

        mesh = assets.getModel("plane");
        shader = assets.getShader("Color");

        borderBreak = 0.0001f;
        transform.rotation.x = (float)Math.toRadians(90.0f);

        fields = new Field[size.x][size.y];
        borders = new Border[size.x][size.y];

        InitializeBoard();
    }

    private void InitializeBoard() {
        int color = 0;
        for(int y = 0; y < size.y; y++){
            for(int x = 0; x < size.x; x++){
                Color[] randC = randomizeColor();
                Color c = randC[0];
                Color bc = randC[1];
                // Field
                Transform t = new Transform();
                t.rotation.set(transform.rotation);
                t.scale.set(transform.scale);
                float posX = x * (2.0f * transform.scale.x);
                float posZ = y * (2.0f * transform.scale.y);
                t.position.set(posX, transform.position.y, posZ);
                fields[x][y] = new Field(c, t, step);
                // Border
                Transform bt = new Transform();
                bt.rotation.set(transform.rotation);
                bt.scale.set(transform.scale);
                bt.position.set(posX, transform.position.y +  borderBreak, posZ);
                borders[x][y] = new Border(bc, bt);
                color++;
                if(color == 3) color = 0;
            }
        }
    }

    private Color[] randomizeColor(){
        Color[] colors = new Color[2];
        int rand = Utils.random.nextInt(10);
        if(rand == 0){
            colors[1] = Color.Red;
            colors[0] = new Color(colors[1].red/step, colors[1].green/step, colors[1].blue/step);
        }else if(rand == 1){
            colors[1] = Color.Green;
            colors[0] = new Color(colors[1].red/step, colors[1].green/step, colors[1].blue/step);
        }else if(rand == 2){
            colors[1] = Color.Blue;
            colors[0] = new Color(colors[1].red/step, colors[1].green/step, colors[1].blue/step);
        }else if(rand == 3){
            colors[1] = Color.Gold;
            colors[0] = new Color(colors[1].red/step, colors[1].green/step, colors[1].blue/step);
        }else if(rand == 4){
            colors[1] = Color.Orange;
            colors[0] = new Color(colors[1].red/step, colors[1].green/step, colors[1].blue/step);
        }else if(rand == 5){
            colors[1] = Color.Cyan;
            colors[0] = new Color(colors[1].red/step, colors[1].green/step, colors[1].blue/step);
        }else if(rand == 6){
            colors[1] = Color.Yellow;
            colors[0] = new Color(colors[1].red/step, colors[1].green/step, colors[1].blue/step);
        }else if(rand == 7){
            colors[1] = Color.Pink;
            colors[0] = new Color(colors[1].red/step, colors[1].green/step, colors[1].blue/step);
        }else if(rand == 8){
            colors[1] = Color.Maroon;
            colors[0] = new Color(colors[1].red/step, colors[1].green/step, colors[1].blue/step);
        }else {
            colors[1] = Color.Magenta;
            colors[0] = new Color(colors[1].red/step, colors[1].green/step, colors[1].blue/step);
        }
        return colors;
    }

    public void setPosition(Vector3f position){
        this.transform.position.set(position);
        InitializeBoard();
    }

    public void setRotation(Vector3f rotation){
        this.transform.rotation.set(rotation);
        InitializeBoard();
    }

    public void setScale(Vector3f scale){
        this.transform.scale.set(scale);
        InitializeBoard();
    }

    public Transform getTransform() {
        return transform;
    }

    public void setColliders(GameEngine ge){
        for(int y = 0; y < size.y; y++){
            for(int x = 0; x < size.x; x++){
                ge.getColliders().add(fields[x][y]);
            }
        }
    }

    public void clearColliders(GameEngine ge){
        for(int y = 0; y < size.y; y++){
            for(int x = 0; x < size.x; x++){
                ge.getColliders().remove(fields[x][y]);
            }
        }
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
        InitializeBoard();
    }

    public void render(){
        shader.bind();
        shader.setInteger(0, "EnableTexture");
        shader.setVec3(graphic.getCamera().getPosition(), "CameraPos");
        shader.setLight(graphic.getLight());
        shader.setMat4(graphic.getCamera().getCameraMatrix(), "Camera");
        DrawField();
        DrawBorder();
        shader.unbind();
    }

    public void DrawField(){
        mesh.bind();
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        for(int y = 0; y < size.y; y++){
            for(int x = 0; x < size.x; x++){
                shader.setModel(fields[x][y]);
                shader.setVec3(fields[x][y].color.toVec3(), "Color");
                shader.setMat4(new Matrix4f().rotateXYZ(fields[x][y].rotation), "Rotation");

                glDrawElements(GL_TRIANGLES, mesh.getCount(), GL_UNSIGNED_INT, 0);
            }
        }
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        mesh.unbind();
    }

    public void DrawBorder(){
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        mesh.bind();
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        for(int y = 0; y < size.y; y++){
            for(int x = 0; x < size.x; x++){
                shader.setModel(borders[x][y].transform);
                shader.setVec3(borders[x][y].color.toVec3(), "Color");
                shader.setMat4(new Matrix4f().rotateXYZ(borders[x][y].transform.rotation), "Rotation");

                glDrawArrays(GL_LINE_STRIP, 0, 5);
            }
        }
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        mesh.unbind();

        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }

    private class Field extends Collider {
        private int meter;
        private final int step;
        private final Color primaryColor;
        public Color color;

        public Field(Color color, Transform transform, int step) {
            super();
            this.color = color;
            this.primaryColor = color;
            this.position = transform.position;
            this.rotation = transform.rotation;
            this.scale = transform.scale;
            this.meter = 0;
            this.step = step;
        }

        @Override
        public void enter(Collider collider) {
            if(collider.getTag().equals("Camera")){
                if(meter < step) {
                    meter++;
                    this.color = this.color.add(primaryColor);
                }
            }
        }

        @Override
        public void collision(Collider collider) {
            if(collider.getTag().equals("Camera")){
                collider.position.y = (transform.position.y + 1.0f) - (1.0f - collider.scale.y);
            }
        }

        @Override
        public void exit(Collider collider) {
        }

        @Override
        public String getTag() {
            return "Field";
        }

        @Override
        public float getUp() {
            return position.y + 0.1f;
        }
        @Override
        public float getDown() {
            return position.y- 0.1f;
        }

        @Override
        public float getLeft() {
            return position.x - (1.0f * scale.x);
        }

        @Override
        public float getRight() {
            return position.x + (1.0f * scale.x);
        }

        @Override
        public float getForward() {
            return position.z + (1.0f *scale.y);
        }

        @Override
        public float getBack() {
            return position.z - (1.0f * scale.y);
        }
    }

    private static class Border{
        public Color color;
        public Transform transform;

        public Border(Color color, Transform transform) {
            this.color = color;
            this.transform = transform;
        }
    }
}
