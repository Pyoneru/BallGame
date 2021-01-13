#version 330

in vec2 Texture;
uniform sampler2D image;

out vec4 EndImage;

void main() {
    EndImage = texture(image, Texture);
}
