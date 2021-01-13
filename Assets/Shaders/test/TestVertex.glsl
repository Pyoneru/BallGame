#version 330
layout (location = 0) in vec3 vertices;
layout (location = 1) in vec2 textures;
layout (location = 2) in vec3 normal;

void main() {

    gl_Position = vec4(vertices, 1.0f);
}
