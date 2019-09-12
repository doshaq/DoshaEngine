package engine.entities

import org.lwjgl.input.Keyboard
import org.lwjgl.util.vector.Vector3f

class Camera (val position:Vector3f = Vector3f(0f,0f,0f), val pitch:Float=0f, val yaw:Float=0f, val roll:Float=0f){


    fun move(){
        if(Keyboard.isKeyDown(Keyboard.KEY_W))
            position.z -= 0.5f
        if(Keyboard.isKeyDown(Keyboard.KEY_S))
            position.z += 0.5f
        if(Keyboard.isKeyDown(Keyboard.KEY_D))
            position.x += 0.5f
        if(Keyboard.isKeyDown(Keyboard.KEY_A))
            position.x -= 0.5f
    }
}