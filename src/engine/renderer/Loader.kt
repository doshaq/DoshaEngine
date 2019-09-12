package engine.renderer

import engine.models.RawModel
import java.nio.FloatBuffer
import java.util.ArrayList
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import org.newdawn.slick.opengl.Texture
import org.newdawn.slick.opengl.TextureLoader
import java.io.FileInputStream
import java.nio.IntBuffer



/**
 * Handles the loading of geometry data into VAOs. It also keeps track of all
 * the created VAOs and VBOs so that they can all be deleted when the game
 * closes.
 *
 * @author Karl
 */
class Loader {

    private val vaos = ArrayList<Int>()
    private val vbos = ArrayList<Int>()
    private val textures = ArrayList<Int>()

    /**
     * Creates a VAO and stores the position data of the vertices into attribute
     * 0 of the VAO. The indices are stored in an index buffer and bound to the
     * VAO.
     *
     * @param positions
     *            - The 3D positions of each vertex in the geometry (in this
     *            example a quad).
     * @param indices
     *            - The indices of the model that we want to store in the VAO.
     *            The indices indicate how the vertices should be connected
     *            together to form triangles.
     *@param textureCoords
     *             - The texture for model , it stores it at attribute number 1 ,
     *             it contains the texture location reference
     * @return The loaded model.
     */
    fun loadToVAO(positions: FloatArray, textureCoords: FloatArray, indices: IntArray,normals:FloatArray): RawModel {
        val vaoID = createVAO()
        bindIndicesBuffer(indices)
        storeDataInAttributeList(0, 3, positions)
        storeDataInAttributeList(1, 2, textureCoords)
        storeDataInAttributeList(2, 3, normals)
        unbindVAO()
        return RawModel(vaoID, indices.size)
    }
    /**
     * load texture to the engine memory
     *
     * @param filename
     *              - the texture file that you want to load
     *
     */
    fun loadTexture(fileName: String): Int {
        var texture: Texture? = null
        try {
            texture = TextureLoader.getTexture("PNG",
                    FileInputStream("res/images/$fileName.png"))
        } catch (e: Exception) {
            e.printStackTrace()
            System.err.println("Tried to load texture $fileName.png , didn't work")
            System.exit(-1)
        }

        textures.add(texture!!.textureID)
        return texture.textureID
    }

    /**
     * Deletes all the VAOs and VBOs when the game is closed. VAOs and VBOs are
     * located in video memory.
     */
    fun cleanUp() {
        for (vao in vaos) {
            GL30.glDeleteVertexArrays(vao)
        }
        for (vbo in vbos) {
            GL15.glDeleteBuffers(vbo)
        }
        for (texture in textures) {
            GL11.glDeleteTextures(texture)
        }
    }



    /**
     * Creates a new VAO and returns its ID. A VAO holds geometry data that we
     * can render and is physically stored in memory on the GPU, so that it can
     * be accessed very quickly during rendering.
     *
     * Like most objects in OpenGL, the new VAO is created using a "gen" method
     * which returns the ID of the new VAO. In order to use the VAO it needs to
     * be made the active VAO. Only one VAO can be active at a time. To make
     * this VAO the active VAO (so that we can store stuff in it) we have to
     * bind it.
     *
     * @return The ID of the newly created VAO.
     */
    private fun createVAO(): Int {
        val vaoID = GL30.glGenVertexArrays()
        vaos.add(vaoID)
        GL30.glBindVertexArray(vaoID)
        return vaoID
    }

    /**
     * Stores the position data of the vertices into attribute 0 of the VAO. To
     * do this the positions must first be stored in a VBO. You can simply think
     * of a VBO as an array of data that is stored in memory on the GPU for easy
     * access during rendering.
     *
     * Just like with the VAO, we create a new VBO using a "gen" method, and
     * make it the active VBO (so that we do stuff to it) by binding it.
     *
     * We then store the positions data in the active VBO by using the
     * glBufferData method. We also indicate using GL_STATIC_DRAW that this data
     * won't need to be changed. If we wanted to edit the positions every frame
     * (perhaps to animate the quad) then we would use GL_DYNAMIC_DRAW instead.
     *
     * We the connect the VBO to the VAO using the glVertexAttribPointer()
     * method. This needs to know the attribute number of the VAO where we want
     * to put the data, the number of floats used for each vertex (3 floats in
     * this case, because each vertex has a 3D position, an x, y, and z value),
     * the type of data (in this case we used floats) and then some other more
     * complicated stuff for storing the data in more fancy ways. Don't worry
     * about the last 3 parameters for now, we don't need them here.
     *
     * Now that we've finished using the VBO we can unbind it. This isn't
     * totally necessary, but I think it's good practice to unbind the VBO when
     * you're done using it.
     *
     * @param attributeNumber
     * - The number of the attribute of the VAO where the data is to
     * be stored.
     * @param data
     * - The geometry data to be stored in the VAO, in this case the
     * positions of the vertices.
     * @param coordinateSize
     * = the size of each coordinate i.e for location its xyz therefore 3 , but for texture its 2 for u,v
     */
    private fun storeDataInAttributeList(attributeNumber: Int, coordinateSize: Int, data: FloatArray) {
        val vboID = GL15.glGenBuffers()
        vbos.add(vboID)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID)
        val buffer = storeDataInFloatBuffer(data)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW)
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
    }

    /**
     * Unbinds the VAO after we're finished using it. If we want to edit or use
     * the VAO we would have to bind it again first.
     */
    private fun unbindVAO() {
        GL30.glBindVertexArray(0)
    }

    /**
     * Creates an index buffer, binds the index buffer to the currently active
     * VAO, and then fills it with our indices.
     *
     * The index buffer is different from other data that we might store in the
     * attributes of the VAO. When we stored the positions we were storing data
     * about each vertex. The positions were "attributes" of each vertex. Data
     * like that is stored in an attribute list of the VAO.
     *
     * The index buffer however does not contain data about each vertex. Instead
     * it tells OpenGL how the vertices should be connected. Each VAO can only
     * have one index buffer associated with it. This is why we don't store the
     * index buffer in a certain attribute of the VAO; each VAO has one special
     * "slot" for an index buffer and simply binding the index buffer binds it
     * to the currently active VAO. When the VAO is rendered it will use the
     * index buffer that is bound to it.
     *
     * This is also why we don't unbind the index buffer, as that would unbind
     * it from the VAO.
     *
     * Note that we tell OpenGL that this is an index buffer by using
     * "GL_ELEMENT_ARRAY_BUFFER" instead of "GL_ARRAY_BUFFER". This is how
     * OpenGL knows to bind it as the index buffer for the current VAO.
     *
     * @param indices
     */
    private fun bindIndicesBuffer(indices: IntArray) {
        val vboID = GL15.glGenBuffers()
        vbos.add(vboID)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID)
        val buffer = storeDataInIntBuffer(indices)
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW)
    }
    /**
     * Converts the indices from an int array to an IntBuffer so that they can
     * be stored in a VBO. Very similar to the storeDataInFloatBuffer() method
     * below.
     *
     * @param data
     *            - The indices in an int[].
     * @return The indices in a buffer.
     */
    private fun storeDataInIntBuffer(data: IntArray): IntBuffer {
        val buffer = BufferUtils.createIntBuffer(data.size)
        buffer.put(data)
        buffer.flip()
        return buffer
    }

    /**
     * Before we can store data in a VBO it needs to be in a certain format: in
     * a buffer. In this case we will use a float buffer because the data we
     * want to store is float data. If we were storing int data we would use an
     * IntBuffer.
     *
     * First and empty buffer of the correct size is created. You can think of a
     * buffer as basically an array with a pointer. After putting the necessary
     * data into the buffer the pointer will have increased so that it points at
     * the first empty element of the array. This is so that we could add more
     * data to the buffer if we wanted and it wouldn't overwrite the data we've
     * already put in. However, we're done with storing data and we want to make
     * the buffer ready for reading. To do this we need to make the pointer
     * point to the start of the data, so that OpenGL knows where in the buffer
     * to start reading. The "flip()" method does just that, putting the pointer
     * back to the start of the buffer.
     *
     * @param data
     * - The float data that is going to be stored in the buffer.
     * @return The FloatBuffer containing the data. This float buffer is ready
     * to be loaded into a VBO.
     */
    private fun storeDataInFloatBuffer(data: FloatArray): FloatBuffer {
        val buffer = BufferUtils.createFloatBuffer(data.size)
        buffer.put(data)
        buffer.flip()
        return buffer
    }

}
