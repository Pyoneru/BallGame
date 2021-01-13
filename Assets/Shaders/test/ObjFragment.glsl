#version 330

uniform vec3 Color;

in vec2 Texture;
uniform sampler2D image;

out vec4 EndColor;

void main() {
    EndColor = texture(image, Texture);
}
