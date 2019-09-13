package demo

import engine.entities.Camera
import engine.entities.Entity
import engine.entities.Light
import engine.models.Model
import engine.renderer.DisplayManager
import engine.renderer.Loader
import engine.renderer.RenderManager
import engine.terrains.Terrain
import engine.textures.Texture
import engine.toolbox.objImporter
import org.lwjgl.opengl.Display
import org.lwjgl.util.vector.Vector3f
import java.util.*
import kotlin.collections.ArrayList


object MainGameLoop {


    @JvmStatic
    fun main(args: Array<String>) {

        DisplayManager.createDisplay()
        val loader = Loader()
        val entities = ArrayList<Entity>()
        val camera = Camera()
        val light = Light(Vector3f(2000f,20000f,2000f), Vector3f(1f,1f,1f))
        val renderManager = RenderManager()
        val bush = Model(objImporter("bush_01",loader), Texture(loader.loadTexture("diffus","tga"),hasTransparenty = true,useFakeLighting = true))
        val random = Random()

        for(i in 0..500){
            entities.add(Entity(bush,Vector3f(random.nextFloat()* 800f - 400f,0f,random.nextFloat() * -600),0f,0f,0f,0.09f))
        }

        val terrain = Terrain(-0.5f,-0.5f,Texture(loader.loadTexture("grass")),loader)
        val terrain2 = Terrain(0.5f,-0.5f,Texture(loader.loadTexture("grass")),loader)
        val terrain3 = Terrain(0.5f,-1f,Texture(loader.loadTexture("grass")),loader)
        val terrain4 = Terrain(-1f,-0.5f,Texture(loader.loadTexture("grass")),loader)
        val terrain5 = Terrain(-1f,-1f,Texture(loader.loadTexture("grass")),loader)
        val terrain6 = Terrain(-0.5f,-1f,Texture(loader.loadTexture("grass")),loader)
        while (!Display.isCloseRequested()) {
            camera.move()
            renderManager.processTerrain(terrain)
            renderManager.processTerrain(terrain2)
            renderManager.processTerrain(terrain3)
            renderManager.processTerrain(terrain4)
            renderManager.processTerrain(terrain5)
            renderManager.processTerrain(terrain6)

            for (entity in entities){
                renderManager.processEntity(entity)
            }

            renderManager.render(light,camera)
            DisplayManager.updateDisplay()
        }

        renderManager.cleanUp()
        loader.cleanUp()
        DisplayManager.closeDisplay()

    }
}
