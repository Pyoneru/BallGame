#version 330

in vec3 FragPos;
in vec3 Normal;

uniform vec3 Color;
uniform vec3 LightColor;
uniform float AmbientStrength;
uniform float SpecularStrength;

uniform vec3 LightPos;
uniform vec3 CameraPos;

out vec4 EndColor;

void main() {
    // Ambient
    vec3 ambient = AmbientStrength * LightColor;

    // Diffuse
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(LightPos - FragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * LightColor;

    // Specular
    vec3 viewDir = normalize(CameraPos - FragPos);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32);
    vec3 specular = SpecularStrength * spec * LightColor;

    vec3 result = (ambient + diffuse + specular) * Color;
    EndColor = vec4(result, 1.0f);
}
