#version 330

uniform vec3 Color;
out vec4 EndColor;

void main() {
    EndColor = vec4(Color, 1.0f);
}
