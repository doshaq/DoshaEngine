package engine.entities

import org.lwjgl.util.vector.Vector3f


abstract class Atom (val position: Vector3f, val rotation:Vector3f, var scale:Float){

    abstract fun onCreate()
    abstract fun update()
}