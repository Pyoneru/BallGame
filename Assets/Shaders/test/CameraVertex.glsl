#version 330
layout (location = 0) in vec3 vertices;
layout (location = 1) in vec2 texture;
layout (location = 2) in vec3 normal;

out vec2 Texture;

uniform mat4 Model;
uniform mat4 Camera;

void main() {
    gl_Position = Camera * Model * vec4(vertices, 1.0f);
    Texture = texture;
}
