#version 330
layout (location = 0) in vec3 vertices;
layout (location = 1) in vec2 texture;
layout (location = 2) in vec3 normal;

out vec3 FragPos;
out vec3 Normal;
out vec2 Texture;

uniform mat4 Camera;
uniform mat4 Model;
uniform mat4 Rotation;

void main() {
    gl_Position = Camera * Model * vec4(vertices, 1.0f);
    FragPos = vec3(Model * vec4(vertices, 1.0f));
    Texture = texture;
    Normal = vec3(Rotation * vec4(normal, 1.0f));
}
