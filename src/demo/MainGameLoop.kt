package demo

import engine.entities.Camera
import engine.entities.Entity
import engine.entities.Light
import engine.renderer.DisplayManager
import engine.renderer.Loader
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
        val shader = StaticShader()
        val renderer = Renderer(shader)

        val model = objImporter("dragon",loader)

        val staticModel = engine.models.TextureModel(model, ModelTexture(loader.loadTexture("white")))
        staticModel.modelTexture.shineDamper =2f
        staticModel.modelTexture.reflectivity =1f
        val entity = Entity(staticModel, Vector3f(0f, -3.5f, -50f), 0f, 180f, 0f, 1f)
        val light = Light(Vector3f(0f,0f,-60f), Vector3f(1f,1f,1f))
        val camera = Camera()

        while (!Display.isCloseRequested()) {
            entity.increaseRotation(0f,0.5f,0f)
            camera.move()
            renderer.prepare()
            shader.start()
            shader.loadLight(light)
            shader.loadViewMatrix(camera)
            renderer.render(entity, shader)
            shader.stop()
            DisplayManager.updateDisplay()
        }

        shader.cleanUp()
        loader.cleanUp()
        DisplayManager.closeDisplay()

    }
}
