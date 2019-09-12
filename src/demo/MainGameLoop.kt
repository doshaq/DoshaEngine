package demo

import engine.entities.Camera
import engine.entities.Entity
import engine.entities.Light
import engine.models.TextureModel
import engine.renderer.DisplayManager
import engine.renderer.Loader
import engine.renderer.RenderManager
import engine.renderer.Renderer
import engine.shaders.StaticShader
import engine.textures.ModelTexture
import engine.toolbox.objImporter
import org.lwjgl.opengl.Display
import org.lwjgl.util.vector.Vector3f




object MainGameLoop {


    @JvmStatic
    fun main(args: Array<String>) {

        DisplayManager.createDisplay()
        val loader = Loader()
        val entities = ArrayList<Entity>()
        val camera:Camera = Camera()
        val renderManager = RenderManager()
        while (!Display.isCloseRequested()) {
            camera.move()

            for (entity in entities){
                renderManager.processEntity(entity)
            }

            DisplayManager.updateDisplay()
        }

        renderManager.cleanUp()
        loader.cleanUp()
        DisplayManager.closeDisplay()

    }
}
