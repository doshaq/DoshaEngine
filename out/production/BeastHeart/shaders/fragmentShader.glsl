#version 400 core


in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

uniform sampler2D modelTexture;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

out vec4 out_Color;

void main(void){
    vec3 unitNormal         = normalize(surfaceNormal);
    vec3 unitLightVector    = normalize(toLightVector);
    vec3 unitCameraVector     = normalize(toCameraVector);

    float nDot1             = dot(unitNormal,unitLightVector);
    float brightness = max(nDot1,0.2);
    vec3 diffuse = brightness * lightColor;
    vec3 lightDirection = - unitCameraVector;
    vec3 reflectedLight = reflect(lightDirection,unitNormal);
    float specularFactor = dot(reflectedLight,unitCameraVector);
    specularFactor = max(specularFactor,0.0);
    float dampedFactor = pow(specularFactor,shineDamper);
    vec3 finalSpecular = dampedFactor * reflectivity * lightColor;

    vec4 textureColor = texture(modelTexture,pass_textureCoordinates);

    if(textureColor.a<0.5){
        discard;
    }

    out_Color =  vec4(diffuse,1.0) * textureColor + vec4(finalSpecular,1.0);
    out_Color = mix(vec4(skyColor,1.0),out_Color,visibility);

}