package engine.shaders

import engine.entities.Camera
import engine.entities.Light
import engine.toolbox.createViewMatrix
import org.lwjgl.util.vector.Matrix4f
import org.lwjgl.util.vector.Vector3f


class StaticShader : ShaderProgram(VERTEX_FILE, FRAGMENT_FILE) {

    private var locationTransformationMatrix: Int = 0
    private var locationProjectionMatrix: Int = 0
    private var locationViewMatrix: Int = 0
    private var locationLightPosition: Int = 0
    private var locationLightColor: Int = 0
    private var locationShineDamper: Int = 0
    private var locationReflectivity: Int = 0
    private var locationUseFakeLighting: Int = 0
    private var locationSkyColor: Int = 0

    override fun bindAttributes() {
        super.bindAttribute(0, "position")
        super.bindAttribute(1, "textureCoordinates")
        super.bindAttribute(2, "normal")
    }

    override fun getAllUniformLocations() {
        locationTransformationMatrix = super.getUniformLocation("transformationMatrix")
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix")
        locationViewMatrix = super.getUniformLocation("viewMatrix")
        locationLightPosition = super.getUniformLocation("lightPosition")
        locationLightColor = super.getUniformLocation("lightColor")
        locationShineDamper = super.getUniformLocation("shineDamper")
        locationReflectivity = super.getUniformLocation("reflectivity")
        locationUseFakeLighting = super.getUniformLocation("useFakeLighting")
        locationSkyColor = super.getUniformLocation("skyColor")

    }
    fun loadSkyColour(r:Float,g:Float,b:Float){
        super.loadVector(locationSkyColor, Vector3f(r,g,b))
    }
    fun loadShineVariables(damper:Float,reflectivity:Float){
       super.loadFloat(locationShineDamper,damper)
       super.loadFloat(locationReflectivity,reflectivity)
    }
    fun loadFakeLighting(useFake:Boolean){
        super.loadBoolean(locationUseFakeLighting,useFake)
    }
    fun loadTransformationMatrix(matrix: Matrix4f) {
        super.loadMatrix(locationTransformationMatrix, matrix)
    }

    fun loadViewMatrix(camera: Camera) {
        val viewMatrix = createViewMatrix(camera)
        super.loadMatrix(locationViewMatrix, viewMatrix)
    }

    fun loadProjectionMatrix(projection: Matrix4f) {
        super.loadMatrix(locationProjectionMatrix, projection)
    }
    fun loadLight(light:Light){
        super.loadVector(locationLightPosition,light.position)
        super.loadVector(locationLightColor,light.color)

    }
    companion object {

        private val VERTEX_FILE = "res/shaders/vertexShader.glsl"
        private val FRAGMENT_FILE = "res/shaders/fragmentShader.glsl"
    }


}