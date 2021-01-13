#version 330

struct Light{
    vec3 position;
    vec3 color;
    float ambient;
    float diffuse;
    float specular;
};

in vec3 FragPos;
in vec3 Normal;
in vec2 Texture;
uniform vec3 Color;
uniform Light light;
uniform vec3 CameraPos;
uniform sampler2D image;
out vec4 Fragment;

void main() {
    vec3 ambient = light.ambient * (light.color * Color);

    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(light.position - FragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = light.diffuse * (diff * Color);

    vec3 viewDir = normalize(CameraPos - FragPos);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32);
    vec3 specular = light.specular * (spec * Color);

    vec3 result = (ambient + diffuse + specular) * Color;
    Fragment = texture(image, Texture) * vec4(result, 1.0f);
}
