package engine.entities

import org.lwjgl.input.Keyboard
import org.lwjgl.util.vector.Vector3f

class Camera (val position:Vector3f = Vector3f(0f,5f,0f), val pitch:Float=10f, val yaw:Float=0f, val roll:Float=0f){


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
            position.y+=0.2f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            position.y-=0.2f;
        }
    }
}