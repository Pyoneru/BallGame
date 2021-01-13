package pl.piotrkniemczuk.ball.assets;

import pl.piotrkniemczuk.ball.engine.Dispose;
import pl.piotrkniemczuk.ball.utils.Utils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Mesh implements Dispose {

    /**
     * Vertex Array Object Id
     */
    private int vao;
    /**
     * Count of vertices to draw
     */
    private int count;

    /**
     * Constructor, generate vao with vbo for vertices, texture, normal map and ebo
     * @param vertices vertices
     * @param texture texture coord
     * @param normal nomral map
     * @param indices Element Array Object
     */
    public Mesh(float[] vertices, float[] texture, float[] normal, int[] indices) {
        this.count = indices.length;

        this.vao = glGenVertexArrays();
        int verticesVBO = glGenBuffers();
        int textureVBO = glGenBuffers();
        int normalVBO = glGenBuffers();
        int indicesEBO = glGenBuffers();

        glBindVertexArray(this.vao);

        glBindBuffer(GL_ARRAY_BUFFER, verticesVBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, textureVBO);
        glBufferData(GL_ARRAY_BUFFER, texture, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, normalVBO);
        glBufferData(GL_ARRAY_BUFFER, normal, GL_STATIC_DRAW);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesEBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);


        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glDeleteBuffers(verticesVBO);
        glDeleteBuffers(textureVBO);
        glDeleteBuffers(normalVBO);
        glDeleteBuffers(indicesEBO);
    }

    /**
     * Bind this vao
     */
    public void bind(){
        glBindVertexArray(this.vao);
    }

    /**
     * Unbind any vao
     */
    public void unbind(){
        glBindVertexArray(0);
    }

    /**
     * Render VAO, bind vao, enable layouts and draw elements
     */
    public void render(){
        glBindVertexArray(this.vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glDrawElements(GL_TRIANGLES, this.count, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }

    /**
     * Get VAO id
     * @return
     */
    public int getVao() {
        return vao;
    }

    /**
     * Get count
     * @return
     */
    public int getCount() {
        return count;
    }

    /**
     * Delete this VAO.
     */
    @Override
    public void clearMemory() {
        glDeleteVertexArrays(this.vao);
    }

    public static Mesh createPlane(){
        float[] vertices = {
                -1.0f, 1.0f, 0.0f,
                -1.0f, -1.0f, 0.0f,
                1.0f, -1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                -1.0f, 1.0f, 0.0f
        };

        float[] texture = {
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f
        };

        float[] normal = {
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f
        };

        int[] indices = {
                0, 1, 2,
                2, 3, 0
        };
        return new Mesh(vertices, texture, normal, indices);
    }
}
