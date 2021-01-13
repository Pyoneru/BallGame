package pl.piotrkniemczuk.ball.assets;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;
import pl.piotrkniemczuk.ball.engine.Transform;
import pl.piotrkniemczuk.ball.graphics.Light;
import pl.piotrkniemczuk.ball.utils.Constants;
import pl.piotrkniemczuk.ball.utils.Utils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Shader {

    /**
     * Shader program id
     */
    private int program;

    /**
     * Private constructor, generate program
     */
    private Shader(){
        this.program = glCreateProgram();
    }

    /**
     * Static method to create new instance Shader class
     * @return new instance of Shader class
     */
    public static Shader create(){
        return new Shader();
    }

    /**
     * Attach Shader from file with type
     * @param path path to file
     * @param type type shader
     * @return return this class
     */
    public Shader attachShader(String path, int type){
        int shader = glCreateShader(type);
        glShaderSource(shader, Utils.readFileToString(path));
        glCompileShader(shader);
        if(glGetShaderi(shader, GL_COMPILE_STATUS) == 0){
            try {
                throw new Exception(glGetShaderInfoLog(shader));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        glAttachShader(this.program, shader);
        glDeleteShader(shader);
        return this;
    }

    /**
     * Link program
     * @return return this class
     */
    public Shader link(){
        glLinkProgram(this.program);
        if(glGetProgrami(this.program, GL_LINK_STATUS) == 0){
            try {
                throw new Exception(glGetProgramInfoLog(this.program));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    /**
     * Bind this program
     */
    public void bind(){
        glUseProgram(this.program);
    }

    /**
     * Unbind any program
     */
    public void unbind(){
        glUseProgram(0);
    }

    /**
     * Send Vector3f to shader
     * @param vector Vector3f
     * @param name Variable name in shader
     */
    public void setVec3(Vector3f vector, String name){
        try(MemoryStack stack = MemoryStack.stackPush()){
            FloatBuffer fb = stack.mallocFloat(3);
            vector.get(fb);
            glUniform3fv(glGetUniformLocation(this.program, name), fb);
        }
    }

    /**
     * Send Matrix4f to shader
     * @param matrix Matrix4f
     * @param name Variable nam in shader
     */
    public void setMat4(Matrix4f matrix, String name){
        try(MemoryStack stack = MemoryStack.stackPush()){
            FloatBuffer fb = stack.mallocFloat(16);
            matrix.get(fb);
            glUniformMatrix4fv(glGetUniformLocation(this.program, name), false, fb);
        }
    }

    /**
     * Send Transform as Model to shader
     * @param transform Model
     */
    public void setModel(Transform transform){
        try(MemoryStack stack = MemoryStack.stackPush()){
            Matrix4f model = new Matrix4f()
                    .translate(transform.position)
                    .rotateXYZ(transform.rotation)
                    .scale(transform.scale);
            FloatBuffer fb = stack.mallocFloat(16);
            model.get(fb);
            glUniformMatrix4fv(glGetUniformLocation(this.program, Constants.SHADER_UNIFORM_MODEL_NAME), false, fb);
        }
    }

    public void setMaterial(Material material){
        try(MemoryStack stack = MemoryStack.stackPush()){
            FloatBuffer ambient = stack.mallocFloat(3);
            FloatBuffer diffuse = stack.mallocFloat(3);
            FloatBuffer specular = stack.mallocFloat(3);
            material.ambient.get(ambient);
            material.diffuse.get(diffuse);
            material.specular.get(specular);
            glUniform3fv(glGetUniformLocation(this.program,"material.ambient"), ambient);
            glUniform3fv(glGetUniformLocation(this.program,"material.diffuse"), diffuse);
            glUniform3fv(glGetUniformLocation(this.program,"material.specular"), specular);
            glUniform1f(glGetUniformLocation(this.program, "material.shininess"), material.shininess);
        }
    }

    public void setLight(Light light){
        try(MemoryStack stack = MemoryStack.stackPush()){
            FloatBuffer position = stack.mallocFloat(3);
            FloatBuffer color = stack.mallocFloat(3);
            light.getPosition().get(position);
            light.getColor().toVec3().get(color);
            glUniform3fv(glGetUniformLocation(this.program, "light.position"), position);
            glUniform3fv(glGetUniformLocation(this.program, "light.color"), color);
            glUniform1f(glGetUniformLocation(this.program, "light.ambient"), light.ambient);
            glUniform1f(glGetUniformLocation(this.program, "light.diffuse"), light.diffuse);
            glUniform1f(glGetUniformLocation(this.program, "light.specular"), light.specular);
        }
    }

    public void setFloat(float value, String name){
        glUniform1f(glGetUniformLocation(this.program, name), value);
    }

    public void setInteger(int value, String name){
        glUniform1i(glGetUniformLocation(this.program, name), value);
    }

    /**
     * Delete this program
     */
    public void clearMemory(){
        long time = Utils.getTime();
        glDeleteProgram(this.program);
        System.out.println("Delete Shader: " + (Utils.getTime() - time));
    }
}
