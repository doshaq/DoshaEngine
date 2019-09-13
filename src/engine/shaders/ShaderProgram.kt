package engine.shaders

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL11
import org.lwjgl.util.vector.Matrix4f
import java.io.IOException
import org.lwjgl.util.vector.Vector3f
import java.io.FileReader
import java.io.BufferedReader
import kotlin.system.exitProcess

abstract class ShaderProgram(vertexFile: String, fragmentFile: String) {

    private val programID: Int
    private val vertexShaderID: Int
    private val fragmentShaderID: Int

    init {
        vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER)
        fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER)
        programID = GL20.glCreateProgram()
        GL20.glAttachShader(programID, vertexShaderID)
        GL20.glAttachShader(programID, fragmentShaderID)
        bindAttributes()
        GL20.glLinkProgram(programID)
        GL20.glValidateProgram(programID)
        getAllUniformLocations()
    }

    protected abstract fun getAllUniformLocations()

    protected fun getUniformLocation(uniformName: String): Int {
        return GL20.glGetUniformLocation(programID, uniformName)
    }

    fun start() {
        GL20.glUseProgram(programID)
    }

    fun stop() {
        GL20.glUseProgram(0)
    }

    fun cleanUp() {
        stop()
        GL20.glDetachShader(programID, vertexShaderID)
        GL20.glDetachShader(programID, fragmentShaderID)
        GL20.glDeleteShader(vertexShaderID)
        GL20.glDeleteShader(fragmentShaderID)
        GL20.glDeleteProgram(programID)
    }

    protected abstract fun bindAttributes()

    protected fun bindAttribute(attribute: Int, variableName: String) {
        GL20.glBindAttribLocation(programID, attribute, variableName)
    }

    protected fun loadFloat(location: Int, value: Float) {
        GL20.glUniform1f(location, value)
    }

    protected fun loadVector(location: Int, vector: Vector3f) {
        GL20.glUniform3f(location, vector.x, vector.y, vector.z)
    }

    protected fun loadBoolean(location: Int, value: Boolean) {
        var toLoad = 0f
        if (value) {
            toLoad = 1f
        }
        GL20.glUniform1f(location, toLoad)
    }

    protected fun loadMatrix(location: Int, matrix: Matrix4f) {
        matrix.store(matrixBuffer)
        matrixBuffer.flip()
        GL20.glUniformMatrix4(location, false, matrixBuffer)
    }

    companion object {

        private val matrixBuffer = BufferUtils.createFloatBuffer(16)

        private fun loadShader(file: String, type: Int): Int {
            val shaderSource = StringBuilder()
            try {
                val reader = BufferedReader(FileReader(file))
                for(line in reader.lines()){
                    shaderSource.append(line).append("//\n")
                }
                reader.close()
            } catch (e: IOException) {
                e.printStackTrace()
                exitProcess(-1)
            }

            val shaderID = GL20.glCreateShader(type)
            GL20.glShaderSource(shaderID, shaderSource)
            GL20.glCompileShader(shaderID)
            if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
                println(GL20.glGetShaderInfoLog(shaderID, 500));
                System.err.println("Could not compile shader!");
                exitProcess(-1);
            }
            return shaderID
        }
    }

}