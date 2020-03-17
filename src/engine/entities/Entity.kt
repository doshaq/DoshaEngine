package engine.entities

import engine.models.Model
import org.lwjgl.util.vector.Vector3f

class Entity (val model:Model, position: Vector3f, rotation: Vector3f, scale: Float): Atom(position, rotation, scale) {


    override fun onCreate() {

    }

    override fun update() {

    }


}