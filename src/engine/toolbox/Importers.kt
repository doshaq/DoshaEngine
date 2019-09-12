package engine.toolbox

import com.sun.xml.internal.fastinfoset.util.StringArray
import engine.models.RawModel
import engine.renderer.Loader
import org.lwjgl.util.vector.Vector2f
import org.lwjgl.util.vector.Vector3f
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.lang.Exception


fun objImporter(fileName:String,loader:Loader):RawModel{
    var fr:FileReader? = null
    try{
        fr = FileReader(File("res/models/$fileName.obj"))
    }catch (e:FileNotFoundException){e.printStackTrace()}

    val reader = BufferedReader(fr!!)

    val vertices = ArrayList<Vector3f>()
    val textures = ArrayList<Vector2f>()
    val normals = ArrayList<Vector3f>()
    val indices = ArrayList<Int>()
    val verticesArray: FloatArray
    var textureArray = FloatArray(0)
    var normalsArray = FloatArray(0)
    val indicesArray: IntArray
    try{
        for(line in reader.lines()){
            val currentLine = line.split(" ")

            if(line.startsWith("v ")){
                val vertex = Vector3f(currentLine[1].toFloat(),currentLine[2].toFloat(),currentLine[3].toFloat())
                vertices.add(vertex)
            }
            else if(line.startsWith("vt ")){
                val texture = Vector2f(currentLine[1].toFloat(),currentLine[2].toFloat())
                textures.add(texture)

            }
            else if(line.startsWith("vn ")){
                val vertex = Vector3f(currentLine[1].toFloat(),currentLine[2].toFloat(),currentLine[3].toFloat())
                normals.add(vertex)
            }
            else if(line.startsWith("f")){
                textureArray = FloatArray(vertices.size * 2)
                normalsArray = FloatArray(vertices.size*3)
                break
            }
        }

        for(line in reader.lines()){
            if(!line.startsWith("f "))
                continue

            val currentLine = line.split(" ")
            val vertex1 = currentLine[1].split("/")
            val vertex2 = currentLine[2].split("/")
            val vertex3 = currentLine[3].split("/")
            // process the whole triangle
            processVertex(vertex1,indices,textures,normals,textureArray,normalsArray)
            processVertex(vertex2,indices,textures,normals,textureArray,normalsArray)
            processVertex(vertex3,indices,textures,normals,textureArray,normalsArray)
        }

        reader.close()
    }catch (e:Exception){e.printStackTrace()}

    verticesArray = FloatArray(vertices.size *3)
    indicesArray = IntArray(indices.size)

    var vertexPointer = 0
    for (vertex in vertices){
        verticesArray[vertexPointer++] = vertex.x
        verticesArray[vertexPointer++] = vertex.y
        verticesArray[vertexPointer++] = vertex.z
    }

    for (i in 0 until indices.size){
        indicesArray[i] = indices[i]
    }

    return loader.loadToVAO(verticesArray,textureArray,indicesArray,normalsArray)
}

fun processVertex(vertexData:List<String>,indices:ArrayList<Int>,textures:List<Vector2f>,normals:List<Vector3f>,textureArray:FloatArray,normalsArray:FloatArray){
    val currentVertexPointer = vertexData[0].toInt() -1

    indices.add(currentVertexPointer)

    // texture is 2d
    val currentTex = textures[vertexData[1].toInt()-1]
    textureArray[currentVertexPointer*2] = currentTex.x
    textureArray[currentVertexPointer*2+1] = 1 - currentTex.y // opengl starts from top left , blender starts from bottom left
    // normals are 3d
    val currentNorm = normals[vertexData[2].toInt()-1]
    normalsArray[currentVertexPointer*3] = currentNorm.x
    normalsArray[currentVertexPointer*3+1] = currentNorm.y
    normalsArray[currentVertexPointer*3+2] = currentNorm.z
}