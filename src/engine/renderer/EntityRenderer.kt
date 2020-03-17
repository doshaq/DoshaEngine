package engine.renderer

import engine.entities.Entity
import engine.models.Model
import engine.shaders.StaticShader
import engine.toolbox.createTransformationMatrix
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import org.lwjgl.util.vector.Matrix4f






/**
 * Handles the rendering of a model to the screen.
 *
 * @author Karl
 */
class EntityRenderer(private val shader: StaticShader, private val projectionMatrix:Matrix4f) {

    init {
        shader.start()
        shader.loadProjectionMatrix(projectionMatrix)
        shader.stop()
    }

    fun render(entities: Map<Model, ArrayList<Entity>>) {
        for (model in entities.keys) {
            prepareTexturedModel(model)
            val batch = entities[model]
            for (entity in batch!!.iterator()) {
                prepareInstance(entity)
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.mesh.vertexCount,
                        GL11.GL_UNSIGNED_INT, 0)
            }
            unbindTexturedModel()
        }
    }

    private fun prepareTexturedModel(model: Model) {
        val rawModel = model.mesh
        GL30.glBindVertexArray(rawModel.vaoID)
        GL20.glEnableVertexAttribArray(0)
        GL20.glEnableVertexAttribArray(1)
        GL20.glEnableVertexAttribArray(2)
        val texture = model.texture
        if(texture.hasTransparenty)
            RenderManager.disableCulling()

        shader.loadFakeLighting(texture.useFakeLighting)
        shader.loadShineVariables(texture.shineDamper, texture.reflectivity)
        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,texture.textureID)
    }

    private fun unbindTexturedModel() {
        RenderManager.enableCulling()
        GL20.glDisableVertexAttribArray(0)
        GL20.glDisableVertexAttribArray(1)
        GL20.glDisableVertexAttribArray(2)
        GL30.glBindVertexArray(0)
    }

    private fun prepareInstance(entity: Entity) {
        val transformationMatrix = createTransformationMatrix(entity.position,
                entity.rotation.x, entity.rotation.y, entity.rotation.z, entity.scale)
        shader.loadTransformationMatrix(transformationMatrix)
    }
}
