package engine.renderer

import engine.entities.Camera
import engine.entities.Entity
import engine.entities.Light
import engine.models.TextureModel
import engine.shaders.StaticShader


class RenderManager{
    val shader = StaticShader()
    val renderer = Renderer(shader)

    val entities:HashMap<TextureModel,ArrayList<Entity>> = HashMap()


    fun renderer(light: Light, camera: Camera){
        renderer.prepare()
        shader.start()
        shader.loadLight(light)
        shader.loadViewMatrix(camera)

        shader.stop()
        entities.clear()

    }

    fun processEntity(entity: Entity){
        val entityModel = entity.model
        val batch = entities[entityModel]
        if(batch!= null)
            batch.add(entity)
        else{
            val newBatch = ArrayList<Entity>()
            newBatch.add(entity)
            entities[entityModel] = newBatch
        }
    }
    fun cleanUp(){
        shader.cleanUp()
    }
}