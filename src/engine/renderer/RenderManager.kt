package engine.renderer

import engine.entities.Camera
import engine.entities.Entity
import engine.entities.Light
import engine.models.Model
import engine.shaders.StaticShader
import engine.shaders.TerrainShader
import engine.terrains.Terrain
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.GL11
import org.lwjgl.util.vector.Matrix4f
import java.util.ArrayList
import java.util.HashMap
import kotlin.math.tan


private val RED = 0.2f
private val GREEN = 0.5f
private val BLUE = 0.6f
class RenderManager{

    companion object{
        fun enableCulling(){
            GL11.glEnable(GL11.GL_CULL_FACE)
            GL11.glCullFace(GL11.GL_BACK)
        }
        fun disableCulling(){
            GL11.glDisable(GL11.GL_CULL_FACE)

        }
    }
    private val FOV = 70f
    private val NEAR_PLANE = 0.1f
    private val FAR_PLANE = 1000f

    private var projectionMatrix: Matrix4f? = null
    private val shader = StaticShader()
    private var renderer: EntityRenderer

    private val terrainShader = TerrainShader()
    private var terrainRenderer: TerrainRenderer


    private val entities = HashMap<Model, ArrayList<Entity>>()
    private val terrains = ArrayList<Terrain>()

    init {
        enableCulling()
        createProjectionMatrix()
        renderer = EntityRenderer(shader, projectionMatrix!!)
        terrainRenderer = TerrainRenderer(terrainShader, projectionMatrix!!)
    }

    fun render(sun: Light, camera: Camera) {
        prepare()
        shader.start()
        shader.loadSkyColour(RED, BLUE, GREEN)
        shader.loadLight(sun)
        shader.loadViewMatrix(camera)
        renderer.render(entities)
        shader.stop()
        terrainShader.start()
        terrainShader.loadSkyColor(RED, BLUE, GREEN)
        terrainShader.loadLight(sun)
        terrainShader.loadViewMatrix(camera)
        terrainRenderer.render(terrains)
        terrainShader.stop()
        terrains.clear()
        entities.clear()
    }

    fun processTerrain(terrain: Terrain) {
        terrains.add(terrain)
    }

    fun processEntity(entity: Entity) {
        val entityModel = entity.model
        val batch = entities[entityModel]
        if (batch != null) {
            batch.add(entity)
        } else {
            val newBatch = ArrayList<Entity>()
            newBatch.add(entity)
            entities[entityModel] = newBatch
        }
    }

    fun cleanUp() {
        shader.cleanUp()
        terrainShader.cleanUp()
    }

    fun prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
        GL11.glClearColor(RED,BLUE, GREEN, 1f)
    }

    private fun createProjectionMatrix() {
        val aspectRatio = Display.getWidth().toFloat() / Display.getHeight().toFloat()
        val yScale = (1f / tan(Math.toRadians((FOV / 2f).toDouble())) * aspectRatio).toFloat()
        val xScale = yScale / aspectRatio
        val frustumLength = FAR_PLANE - NEAR_PLANE

        projectionMatrix = Matrix4f()
        projectionMatrix!!.m00 = xScale
        projectionMatrix!!.m11 = yScale
        projectionMatrix!!.m22 = -((FAR_PLANE + NEAR_PLANE) / frustumLength)
        projectionMatrix!!.m23 = -1f
        projectionMatrix!!.m32 = -(2f * NEAR_PLANE * FAR_PLANE / frustumLength)
        projectionMatrix!!.m33 = 0f
    }
}