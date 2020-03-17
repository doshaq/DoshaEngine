package demo

import engine.entities.Camera
import engine.entities.Entity
import engine.entities.Light
import engine.models.Model
import engine.renderer.DisplayManager
import engine.renderer.Loader
import engine.renderer.RenderManager
import engine.terrains.Terrain
import engine.textures.TerrainTexture
import engine.textures.TerrainTexturePack
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
        //val position:Vector3f = Vector3f(0f,5f,0f), val pitch:Float=10f, val yaw:Float=0f, val roll:Float=0f

        val camera = Camera(Vector3f(0f,5f,0f),Vector3f(10f,0f,0f),1f)
        val light = Light(Vector3f(2000f,20000f,2000f), Vector3f(1f,1f,1f))
        val renderManager = RenderManager()
        val bush = Model(objImporter("bush_01",loader), Texture(loader.loadTexture("diffus","tga"),hasTransparenty = true,useFakeLighting = true))
        val random = Random()

        for(i in 0..500){
            entities.add(Entity(bush,Vector3f(random.nextFloat()* 800f - 400f,0f,random.nextFloat() * -600), Vector3f(0f,0f,0f),0.09f))
        }
        /***************************************Terrain stuff********************************************************/
        val terrainBackgroundTexture = TerrainTexture(loader.loadTexture("grass"))
        val terrainrTexture = TerrainTexture(loader.loadTexture("dirt"))
        val terraingTexture = TerrainTexture(loader.loadTexture("grass"))
        val terrainbTexture = TerrainTexture(loader.loadTexture("flowers"))
        val terrainBlendMapTexture = TerrainTexture(loader.loadTexture("blendmap"))
        val terrain = Terrain(-0.5f,-0.5f,
                TerrainTexturePack(terrainBackgroundTexture,terrainrTexture,terraingTexture,terrainbTexture),
                terrainBlendMapTexture,
                loader)
        /***************************************Terrain stuff********************************************************/

        while (!Display.isCloseRequested()) {
            camera.move()
            renderManager.processTerrain(terrain)


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
