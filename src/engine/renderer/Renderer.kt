package engine.renderer

import engine.entities.Entity
import engine.models.TextureModel
import engine.shaders.StaticShader
import engine.toolbox.createTransformationMatrix
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.Display
import org.lwjgl.util.vector.Matrix4f




/**
 * Handles the rendering of a model to the screen.
 *
 * @author Karl
 */
class Renderer(val shader: StaticShader) {

    private var projectionMatrix: Matrix4f? = null

    init {
        createProjectionMatrix()
        shader.start()
        shader.loadProjectionMatrix(projectionMatrix!!)
        shader.stop()
    }

    fun prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glCullFace(GL11.GL_BACK)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
        GL11.glClearColor(0f, 0.3f, 0.0f, 1f)
    }

    fun render(entities:HashMap<TextureModel,List<Entity>>){
        for (model in entities.keys){
            prepareTextureModel(model)
            val batch:List<Entity> = entities[model]!!
            for (entity in batch){
                prepareInstance(entity)
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.rawModel.vertexCount, GL11.GL_UNSIGNED_INT, 0)

            }
            unbindTextureModel()

        }
    }

    fun prepareInstance(entity: Entity){
        val transformationMatrix = createTransformationMatrix(entity.position,
                entity.rotX, entity.rotY, entity.rotZ, entity.scale)
        shader.loadTransformationMatrix(transformationMatrix)
    }
    fun unbindTextureModel(){
        GL20.glDisableVertexAttribArray(0)
        GL20.glDisableVertexAttribArray(1)
        GL20.glDisableVertexAttribArray(2)
        GL30.glBindVertexArray(0)
    }
    fun prepareTextureModel(textureModel: TextureModel){
        val rawModel = textureModel.rawModel
        GL30.glBindVertexArray(rawModel.vaoID)
        GL20.glEnableVertexAttribArray(0)
        GL20.glEnableVertexAttribArray(1)
        GL20.glEnableVertexAttribArray(2)
        val texture = textureModel.modelTexture
        shader.loadShineVariables(texture.shineDamper,texture.reflectivity)
        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureModel.modelTexture.textureID)
    }
    private fun createProjectionMatrix() {
        val aspectRatio = Display.getWidth().toFloat() / Display.getHeight().toFloat()
        val y_scale = (1f / Math.tan(Math.toRadians((FOV / 2f).toDouble())) * aspectRatio).toFloat()
        val x_scale = y_scale / aspectRatio
        val frustum_length = FAR_PLANE - NEAR_PLANE

        projectionMatrix = Matrix4f()
        projectionMatrix!!.m00 = x_scale
        projectionMatrix!!.m11 = y_scale
        projectionMatrix!!.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length)
        projectionMatrix!!.m23 = -1f
        projectionMatrix!!.m32 = -(2f * NEAR_PLANE * FAR_PLANE / frustum_length)
        projectionMatrix!!.m33 = 0f
    }

    companion object {

        private val FOV = 70f
        private val NEAR_PLANE = 0.1f
        private val FAR_PLANE = 1000f
    }

}
