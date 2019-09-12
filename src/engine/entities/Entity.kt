package engine.entities

import engine.models.TexturedModel
import org.lwjgl.util.vector.Vector3f

class Entity (val model:TexturedModel, val position:Vector3f, var rotX:Float, var rotY:Float, var rotZ:Float,var scale:Float ){



    fun increasePosition(dx:Float,dy:Float,dz:Float){
        this.position.x +=dx
        this.position.y +=dy
        this.position.z +=dz
    }
    fun increaseRotation(dx:Float,dy:Float,dz:Float){
        this.rotX +=dx
        this.rotY +=dy
        this.rotZ +=dz
    }


}