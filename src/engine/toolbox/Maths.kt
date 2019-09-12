package engine.toolbox

import engine.entities.Camera
import org.lwjgl.util.vector.Matrix4f
import org.lwjgl.util.vector.Vector3f


fun createTransformationMatrix(translation: Vector3f, rx: Float, ry: Float,
                               rz: Float, scale: Float): Matrix4f {
    val matrix = Matrix4f()
    matrix.setIdentity()
    Matrix4f.translate(translation, matrix, matrix)
    Matrix4f.rotate(Math.toRadians(rx.toDouble()).toFloat(), Vector3f(1f, 0f, 0f), matrix, matrix)
    Matrix4f.rotate(Math.toRadians(ry.toDouble()).toFloat(), Vector3f(0f, 1f, 0f), matrix, matrix)
    Matrix4f.rotate(Math.toRadians(rz.toDouble()).toFloat(), Vector3f(0f, 0f, 1f), matrix, matrix)
    Matrix4f.scale(Vector3f(scale, scale, scale), matrix, matrix)
    return matrix
}

fun createViewMatrix(camera: Camera): Matrix4f {
    val viewMatrix = Matrix4f()
    viewMatrix.setIdentity()
    Matrix4f.rotate(Math.toRadians(camera.pitch.toDouble()).toFloat(), Vector3f(1f, 0f, 0f), viewMatrix,
            viewMatrix)
    Matrix4f.rotate(Math.toRadians(camera.yaw.toDouble()).toFloat(), Vector3f(0f, 1f, 0f), viewMatrix, viewMatrix)
    val cameraPos = camera.position
    val negativeCameraPos = Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z)
    Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix)
    return viewMatrix
}