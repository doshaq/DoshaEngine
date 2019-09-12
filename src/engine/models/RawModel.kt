package engine.models

/**
 * Represents a loaded model. It contains the ID of the VAO that contains the
 * model's data, and holds the number of vertices in the model.
 *
 * @author Karl
 */
class RawModel(
        /**
         * @return The ID of the VAO which contains the data about all the geometry
         * of this model.
         */
        val vaoID: Int,
        /**
         * @return The number of vertices in the model.
         */
        val vertexCount: Int)