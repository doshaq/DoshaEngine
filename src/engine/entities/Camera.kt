package engine.entities

import org.lwjgl.input.Keyboard
import org.lwjgl.util.vector.Vector3f
//val position:Vector3f = Vector3f(0f,5f,0f), val pitch:Float=10f, val yaw:Float=0f, val roll:Float=0f
class Camera (position: Vector3f, rotation: Vector3f, scale: Float): Atom(position, rotation, scale) {
    var pitch:Float = rotation.x
    var yaw:Float= rotation.y
    var roll:Float=rotation.z
    fun move(){
        if(Keyboard.isKeyDown(Keyboard.KEY_W))
            position.z -= 0.5f
        if(Keyboard.isKeyDown(Keyboard.KEY_S))
            position.z += 0.5f
        if(Keyboard.isKeyDown(Keyboard.KEY_D))
            position.x += 0.5f
        if(Keyboard.isKeyDown(Keyboard.KEY_A))
            position.x -= 0.5f
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            position.y+=0.2f
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            position.y-=0.2f
        }
    }

    override fun onCreate() {

    }

    override fun update() {
        var pitch:Float = rotation.x
        var yaw:Float= rotation.y
        var roll:Float=rotation.z
        move()
    }
}