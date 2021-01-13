package pl.piotrkniemczuk.ball.engine;

import org.joml.Vector2i;
import org.joml.Vector3f;
import pl.piotrkniemczuk.ball.assets.Assets;
import pl.piotrkniemczuk.ball.graphics.Color;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameSettings {

    public class TexturePair{
        public String texture;
        public Assets.ImageType type;

        public TexturePair(String texture, Assets.ImageType type) {
            this.texture = texture;
            this.type = type;
        }
    }

    private boolean enableLight;
    private Color lightColor;
    private Vector3f lightPos;
    private float lightAmbient;
    private float lightDiffuse;
    private float lightSpecular;
    private float lightScale;
    private String cameraType;
    private Vector3f cameraPos;
    private List<String> shaders;
    private List<String> models;
    private List<TexturePair> textures;
    private Vector2i windowSize;
    private String title = "";
    private boolean fullscreen;
    private int fps;

    public GameSettings(String setting){
        shaders = new ArrayList<>();
        models = new ArrayList<>();
        textures = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(setting));
            String line = "";
            while((line = reader.readLine()) != null){
                String[] tokens = line.split("\\s+");
                switch(tokens[0]){
                    case "EnableLight":
                        enableLight = tokens[1].equals("true");
                        break;

                    case "LightColor":
                        lightColor = new Color(
                                Float.parseFloat(tokens[1]),
                                Float.parseFloat(tokens[2]),
                                Float.parseFloat(tokens[3]));
                        break;

                    case "LightPos":
                        lightPos = new Vector3f(
                                Float.parseFloat(tokens[1]),
                                Float.parseFloat(tokens[2]),
                                Float.parseFloat(tokens[3])
                        );

                    case "LightAmbient":
                        lightAmbient = Float.parseFloat(tokens[1]);
                        break;

                    case "LightDiffuse":
                        lightDiffuse = Float.parseFloat(tokens[1]);
                        break;

                    case "LightSpecular":
                        lightSpecular = Float.parseFloat(tokens[1]);
                        break;

                    case "LightScale":
                        lightScale = Float.parseFloat(tokens[1]);
                        break;

                    case "CameraType":
                        cameraType = tokens[1];
                        break;

                    case "CameraPosition":
                        cameraPos = new Vector3f(
                                Float.parseFloat(tokens[1]),
                                Float.parseFloat(tokens[2]),
                                Float.parseFloat(tokens[3])
                        );
                        break;

                    case "Shader":
                        shaders.add(tokens[1]);
                        break;

                    case "Model":
                        models.add(tokens[1]);
                        break;

                    case "Texture":
                        textures.add(new TexturePair(
                                tokens[1],
                                Assets.ImageType.valueOf(tokens[2].toUpperCase())));
                        break;

                    case "WindowSize":
                        windowSize = new Vector2i(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                        break;

                    case "Title":
                        for( int i = 1; i < tokens.length; i++) {
                            title += tokens[i];
                            if(i < tokens.length - 1) title += " ";
                        }
                        break;

                    case "Fullscreen":
                        fullscreen = tokens[1].equals("true");
                        break;

                    case "FPS":
                        fps = Integer.parseInt(tokens[1]);
                        break;

                    default:
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isEnableLight() {
        return enableLight;
    }

    public Color getLightColor() {
        return lightColor;
    }

    public String getCameraType() {
        return cameraType;
    }

    public Vector3f getCameraPos() {
        return cameraPos;
    }

    public List<String> getShaders() {
        return shaders;
    }

    public List<String> getModels() {
        return models;
    }

    public List<TexturePair> getTextures() {
        return textures;
    }

    public Vector2i getWindowSize() {
        return windowSize;
    }

    public String getTitle() {
        return title;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public Vector3f getLightPos() {
        return lightPos;
    }

    public float getLightAmbient() {
        return lightAmbient;
    }

    public float getLightDiffuse() {
        return lightDiffuse;
    }

    public float getLightSpecular() {
        return lightSpecular;
    }

    public float getLightScale() {
        return lightScale;
    }

    public int getFPS() {
        return fps;
    }
}
