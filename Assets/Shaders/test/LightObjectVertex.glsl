#version 330
layout (location = 0) in vec3 vertices;
layout (location = 1) in vec2 texture;
layout (location = 2) in vec3 normal;

out vec3 FragPos;
out vec3 Normal;

uniform mat4 Camera;
uniform mat4 Model;

void main() {
    gl_Position = Camera * Model * vec4(vertices, 1.0f);
    FragPos = vec3(Model * vec4(vertices, 1.0f));
    Normal = mat3(transpose(inverse(Model))) * normal;
}
