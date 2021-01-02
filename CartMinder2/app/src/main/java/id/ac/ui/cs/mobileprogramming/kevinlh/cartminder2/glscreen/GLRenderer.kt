package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen

import android.opengl.GLSurfaceView
import android.opengl.GLU
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.shape.ShapeDrawer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GLRenderer : GLSurfaceView.Renderer {
    private val shapes = ShapeDrawer.getShapes()

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        gl.glShadeModel(GL10.GL_SMOOTH)
        gl.glClearColor(248 / 255f, 239 / 255f, 228 / 255f, 1.0f) // Light Background
        gl.glClearDepthf(1.0f)
        gl.glEnable(GL10.GL_DEPTH_TEST)
        gl.glDepthFunc(GL10.GL_LEQUAL)

        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST)
    }

    override fun onDrawFrame(gl: GL10) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
        gl.glLoadIdentity()

        //Translate to screen 10f
        gl.glTranslatef(0f, 0f, -100f)

        shapes.forEach {
            it.draw(gl)
        }
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, _height: Int) {
        val height = if (_height == 0) 1 else _height // Prevent A Divide By Zero

        gl.glViewport(0, 0, width, height)
        gl.glMatrixMode(GL10.GL_PROJECTION)
        gl.glLoadIdentity()

        //Calculate The Aspect Ratio Of The Window
        GLU.gluPerspective(gl, 45.0f, width.toFloat() / height.toFloat(), 0.1f, 100.0f)
        gl.glMatrixMode(GL10.GL_MODELVIEW)
        gl.glLoadIdentity()
    }
}