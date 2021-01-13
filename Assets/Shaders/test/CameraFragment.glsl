#version 330

in vec2 Texture;
uniform vec3 Color;
uniform sampler2D image;
out vec4 EndColor;

void main() {
    EndColor = texture(image, Texture) * vec4(Color, 1.0f);
}
