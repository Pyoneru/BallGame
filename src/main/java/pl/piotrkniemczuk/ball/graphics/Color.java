package pl.piotrkniemczuk.ball.graphics;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Objects;

public class Color {

    public float red, green, blue, alpha;

    public static Color Red = new Color(255, 0, 0);
    public static Color Green = new Color(0, 255, 0);
    public static Color Blue = new Color(0, 0, 255);
    public static Color White = new Color(255, 255, 255);
    public static Color Black = new Color(0, 0, 0);
    public static Color Gold = new Color(255, 215, 0);
    public static Color Orange = new Color(255, 165, 0);
    public static Color Cyan = new Color(0, 255, 255);
    public static Color Yellow = new Color(255, 255, 0);
    public static Color Pink = new Color(255, 192, 203);
    public static Color Maroon = new Color(128, 0, 0);
    public static Color Magenta = new Color(255, 0, 255);

    public Color(float red, float green, float blue){
        this(red, green, blue, 1.0f);
    }

    public Color(float red, float green, float blue, float alpha){
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Color(){
        this(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public Color(int red, int green, int blue){
        this(red/255.0f, green/255.0f, blue/255.0f, 1.0f);
    }

    public Color(int red, int green, int blue, int alpha){
        this(red/255.0f, green/255.0f, blue/255.0f, alpha/255.0f);
    }

    public Color add(Color color){
        float r = red + color.red;
        float g = green + color.green;
        float b = blue + color.blue;
        if(r > 1.0f) r = 1.0f;
        if(g > 1.0f) g = 1.0f;
        if(b > 1.0f) b = 1.0f;
        return new Color(r, g, b, alpha);
    }

    public Color minus(Color color){
        float r = red - color.red;
        float g = green - color.green;
        float b = blue - color.blue;
        if(r < 0.0f) r = 0.0f;
        if(g < 0.0f) g = 0.0f;
        if(b < 0.0f) b = 0.0f;
        return new Color(r, g, b, alpha);
    }

    public Vector3f toVec3(){
        return new Vector3f(red, green, blue);
    }

    public Vector4f toVec4(){
        return new Vector4f(red, green, blue, alpha);
    }

    @Override
    public String toString() {
        return "Color{" +
                "red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                ", alpha=" + alpha +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return Float.compare(color.red, red) == 0 &&
                Float.compare(color.green, green) == 0 &&
                Float.compare(color.blue, blue) == 0 &&
                Float.compare(color.alpha, alpha) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(red, green, blue, alpha);
    }
}
