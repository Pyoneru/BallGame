#version 330

struct Material{
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
};

struct Light{
    vec3 position;
    vec3 color;
    float ambient;
    float diffuse;
    float specular;
};

in vec3 FragPos;
in vec2 Texture;
in vec3 Normal;

uniform Material material;
uniform Light light;

uniform vec3 CameraPos;
uniform sampler2D image;
uniform int EnableTexture;

out vec4 Fragment;


void main() {
    // Ambient
    vec3 ambient = light.ambient * (light.color * material.ambient);

    // Diffuse
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(light.position - FragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = light.diffuse * (diff * light.color * material.diffuse);

    // Specular
    vec3 viewDir = normalize(CameraPos - FragPos);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec3 specular = light.specular * (spec * light.color * material.specular);

    vec3 result = ambient + diffuse + specular;

    if(EnableTexture == 1){
        Fragment = texture(image, Texture) * vec4(result, 1.0f);
    }else{
        Fragment = vec4(result, 1.0f);
    }

}
