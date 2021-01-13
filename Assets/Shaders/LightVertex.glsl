#version 330
layout (location = 0) in vec3 vertices;

uniform mat4 Camera;
uniform mat4 Model;

void main() {
    gl_Position = Camera * Model * vec4(vertices, 1.0f);
}
