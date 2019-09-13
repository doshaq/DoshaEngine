#version 400 core


in vec3 position;
in vec2 textureCoordinates;
in vec3 normal;

const float density = 0.0035;
const float gradient = 5.0;

out vec3 surfaceNormal;
out vec3 toLightVector;
out vec2 pass_textureCoordinates;
out vec3 toCameraVector;
out float visibility;
uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;
uniform float useFakeLighting;

void main(void){

    vec4 worldPosition = transformationMatrix * vec4(position,1);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;
    pass_textureCoordinates = textureCoordinates;
    vec4 positionRelativeToCamera = viewMatrix * worldPosition;

    vec3 actualNormal = normal;
    if(useFakeLighting >0.5){
        actualNormal = vec3(0,1,0);
    }
    surfaceNormal  = (transformationMatrix *vec4( actualNormal,0.0)).xyz;
    toLightVector  = lightPosition - worldPosition.xyz;
    toCameraVector = (inverse(viewMatrix) * vec4(0,0,0,1)).xyz - worldPosition.xyz;

    float distance = length(positionRelativeToCamera.xyz);
    visibility  = exp(-pow((distance * density ), gradient));
    visibility = clamp(visibility,0.0f,1.0f);
}