package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.shape.rectangle

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.shape.Colors
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.shape.Pos
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.shape.Shape
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.opengles.GL10

class Rectangle(
    override var pos: Pos = Pos(0f, 0f),
    var width: Float = 3f,
    var height: Float = 2f
) : Shape {

    private var vertices: FloatArray = calcVertices()
    private var vertexBuffer: FloatBuffer = calcVertexBuffer()

    override var degreeRotation: Float = 0f
    override var color = Colors.YELLOW

    fun setPos(x: Float, y: Float) {
        pos.x = x
        pos.y = y
    }

    private fun calcVertexBuffer(): FloatBuffer = ByteBuffer
        .allocateDirect(vertices.size * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer().apply {
            put(vertices)
            position(0)
        }

    private fun calcVertices(): FloatArray {
        val xl = pos.x - width / 2   // left x
        val xr = pos.x + width / 2   // right x
        val yb = pos.y - height / 2   // bottom y
        val yt = pos.y + height / 2   // top y
        return floatArrayOf(
            xl, yb, 0.0f,  //Bottom Left
            xr, yb, 0.0f,  //Bottom Right
            xl, yt, 0.0f,  //Top Left
            xr, yt, 0.0f //Top Right
        )
    }

    private fun rotateVertices(vArr: FloatArray): FloatArray {
        val tl = Pos(vArr[0], vArr[1]).rotateFromAnchor(pos, degreeRotation)
        val bl = Pos(vArr[3], vArr[4]).rotateFromAnchor(pos, degreeRotation)
        val br = Pos(vArr[6], vArr[7]).rotateFromAnchor(pos, degreeRotation)
        val tr = Pos(vArr[9], vArr[10]).rotateFromAnchor(pos, degreeRotation)
        return floatArrayOf(
            tl.x, tl.y, 0.0f,  //Bottom Left
            bl.x, bl.y, 0.0f,  //Bottom Right
            br.x, br.y, 0.0f,  //Top Left
            tr.x, tr.y, 0.0f //Top Right
        )
    }

    override fun update() {
        vertices = rotateVertices(calcVertices())
        vertexBuffer = calcVertexBuffer()
    }

    override fun draw(gl: GL10) {
        //Set The Color
        gl.glColor4f(color[0], color[1], color[2], color[3])

        //Set the face rotation
        gl.glFrontFace(GL10.GL_CW)

        //Point to our vertex buffer
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer)

        //Enable vertex buffer
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)

        //Draw the vertices as triangle strip
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.size / 3)

        //Disable the client state before leaving
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)
    }
}