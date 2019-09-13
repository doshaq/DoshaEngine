package engine.renderer

import engine.shaders.TerrainShader
import engine.terrains.Terrain
import engine.toolbox.createTransformationMatrix
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import org.lwjgl.util.vector.Matrix4f
import org.lwjgl.util.vector.Vector3f



class TerrainRenderer(private val shader: TerrainShader = TerrainShader(), projectionMatrix4f: Matrix4f) {



    init {
        shader.start()
        shader.loadProjectionMatrix(projectionMatrix4f)
        shader.stop()
    }

    fun render(terrains: List<Terrain>) {
        for (terrain in terrains) {
            prepareTerrain(terrain)
            loadModelMatrix(terrain)
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.model.vertexCount,
                    GL11.GL_UNSIGNED_INT, 0)
            unbindTexturedModel()
        }
    }

    private fun prepareTerrain(terrain: Terrain) {
        val rawModel = terrain.model
        GL30.glBindVertexArray(rawModel.vaoID)
        GL20.glEnableVertexAttribArray(0)
        GL20.glEnableVertexAttribArray(1)
        GL20.glEnableVertexAttribArray(2)
        val texture = terrain.texture
        shader.loadShineVariables(texture.shineDamper, texture.reflectivity)
        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.textureID)
    }

    private fun unbindTexturedModel() {
        GL20.glDisableVertexAttribArray(0)
        GL20.glDisableVertexAttribArray(1)
        GL20.glDisableVertexAttribArray(2)
        GL30.glBindVertexArray(0)
    }

    private fun loadModelMatrix(terrain: Terrain) {
        val transformationMatrix = createTransformationMatrix(
                Vector3f(terrain.x, 0f, terrain.z), 0f, 0f, 0f, 1f)
        shader.loadTransformationMatrix(transformationMatrix)
    }

}