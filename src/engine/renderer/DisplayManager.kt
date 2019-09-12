package engine.renderer

import org.lwjgl.LWJGLException
import org.lwjgl.opengl.*
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.Display.setTitle


object DisplayManager {

    private val WIDTH = 1280
    private val HEIGHT = 720
    private val FPS_CAP = 120

    fun createDisplay() {
        val attribs = ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true)

        try {
            Display.setDisplayMode(DisplayMode(WIDTH, HEIGHT))
            Display.create(PixelFormat(), attribs)
            Display.setTitle("deez nutz!")
        } catch (e: LWJGLException) {
            e.printStackTrace()
        }

        GL11.glViewport(0, 0, WIDTH, HEIGHT)
    }

    fun updateDisplay() {

        Display.sync(FPS_CAP)
        Display.update()

    }

    fun closeDisplay() {

        Display.destroy()

    }

}