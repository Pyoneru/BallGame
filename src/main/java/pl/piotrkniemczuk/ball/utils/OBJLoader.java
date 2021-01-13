package pl.piotrkniemczuk.ball.utils;

import org.joml.Vector2f;
import org.joml.Vector3f;
import pl.piotrkniemczuk.ball.assets.Mesh;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of OBJ model format loader
 */
public class OBJLoader {


    /**
     * Load obj and create Mesh
     * @param path path to obj file
     * @return mesh created from obj
     */
    public static Mesh loadModel(String path){
        Mesh mesh = null;
        try {
            List<Vector3f> vertices = new ArrayList<>();
            List<Vector2f> texture = new ArrayList<>();
            List<Vector3f> normal = new ArrayList<>();
            List<Face> faces = new ArrayList<>();

            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = "";
            while((line = reader.readLine()) != null){
                String[] tokens = line.split("\\s+");
                switch(tokens[0]){
                    case "v":
                        vertices.add(new Vector3f(
                                Float.parseFloat(tokens[1]),
                                Float.parseFloat(tokens[2]),
                                Float.parseFloat(tokens[3])
                        ));
                        break;

                    case "vt":
                        texture.add(new Vector2f(
                                Float.parseFloat(tokens[1]),
                                Float.parseFloat(tokens[2])
                        ));
                        break;

                    case "vn":
                        normal.add(new Vector3f(
                                Float.parseFloat(tokens[1]),
                                Float.parseFloat(tokens[2]),
                                Float.parseFloat(tokens[3])
                        ));
                        break;

                    case "f":
                        faces.add(new Face(
                                tokens[1],
                                tokens[2],
                                tokens[3]
                        ));
                        break;

                    default:
                        break;
                }
            }

            // Reorder list
            float[] verticesArray = new float[vertices.size() * 3];
            int i = 0;
            for(Vector3f v : vertices){
                verticesArray[i] = v.x;
                verticesArray[i+1] = v.y;
                verticesArray[i+2] = v.z;
                i += 3;
            }

            float[] textureArray = new float[vertices.size() * 2];
            float[] normalArray = new float[vertices.size() * 3];
            List<Integer> indices = new ArrayList<>();
            for(Face f : faces){
                Group[] groups = f.getGroups();
                for(Group g : groups){
                    int index = g.indices;
                    indices.add(index);
                    if(g.texture >= 0){
                        Vector2f textCoord = texture.get(g.texture);
                        textureArray[index * 2] =  textCoord.x;
                        textureArray[index * 2 + 1] = textCoord.y;
                    }
                    if(g.normal >= 0){
                        Vector3f vecNorm = normal.get(g.normal);
                        normalArray[index * 3] = vecNorm.x;
                        normalArray[index * 3 + 1] = vecNorm.y;
                        normalArray[index * 3 + 2] = vecNorm.z;
                    }
                }
            }

            int[] indicesArray = indices.stream().mapToInt((Integer v) -> v).toArray();
            mesh = new Mesh(verticesArray, textureArray, normalArray, indicesArray);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mesh;
    }

    /**
     * Inner class to map face
     */
    private static class Face{

        private Group[] groups;

        public Face(String f1, String f2, String f3){
            this.groups = new Group[3];

            groups[0] = parseData(f1);
            groups[1] = parseData(f2);
            groups[2] = parseData(f3);
        }

        private Group parseData(String data){
            String[] tokens = data.split("/");

            return new Group(
                    Integer.parseInt(tokens[0]) - 1,
                    Integer.parseInt(tokens[1]) - 1,
                    Integer.parseInt(tokens[2]) - 1);
        }

        public Group[] getGroups() {
            return groups;
        }
    }

    /**
     * Inner class to map group of face
     */
    private static class Group{
        public static final int EMPTY = -1;
        public int indices;
        public int texture;
        public int normal;
        public Group(){
            this.indices = EMPTY;
            this.texture = EMPTY;
            this.normal = EMPTY;
        }

        public Group(int indices, int texture, int normal) {
            this.indices = indices;
            this.texture = texture;
            this.normal = normal;
        }
    }
}
