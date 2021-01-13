package pl.piotrkniemczuk.ball.assets;

import org.lwjgl.system.MemoryStack;
import pl.piotrkniemczuk.ball.utils.Utils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {

    /**
     * Texture Id
     */
    private int textureId;
    /**
     * Width image
     */
    private int width;
    /**
     * Height image
     */
    private int height;
    /**
     * Channels image
     */
    private int channels;

    public Texture(String path){
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer data = null;
        try(MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer w = stack.callocInt(1);
            IntBuffer h = stack.callocInt(1);
            IntBuffer c = stack.callocInt(1);
            data = stbi_load(path, w, h, c, 0);
            this.width = w.get();
            this.height = h.get();
            this.channels = c.get();
        }

        this.textureId = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, this.textureId);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        if(channels == 4){
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
        }else if(channels == 3){
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, data);
        }else{
            try {
                throw new Exception("Can not recognize channels value: " + channels);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        glGenerateMipmap(GL_TEXTURE_2D);
        stbi_image_free(data);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    /**
     * Bind this texture
     */
    public void bind(){
        glBindTexture(GL_TEXTURE_2D, this.textureId);
    }

    /**
     * Unbind any texture
     */
    public void unbind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    /**
     * Get texture id
     * @return texture Id
     */
    public int getTextureId() {
        return textureId;
    }

    /**
     * Get width image
     * @return width image
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get height image
     * @return height image
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get channels image
     * @return channels image
     */
    public int getChannels() {
        return channels;
    }


    public void clearMemory(){
        long time = Utils.getTime();
        glDeleteTextures(this.textureId);
        System.out.println("Delete Texture" + (Utils.getTime() - time));
    }
}
