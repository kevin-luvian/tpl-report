package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.shape.simplepolygon

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.shape.Colors
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.shape.Pos
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.shape.Shape
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.opengles.GL10

class SimplePolygon(
    override var pos: Pos,
    verticesCount: Int,
    var radius: Float = 5f
) : Shape {
    override var degreeRotation: Float = 0f
    override var color: FloatArray = Colors.BLUE
    private val verticesDeg: Float = 360f / verticesCount
    private val verticesPos: List<Pos> = List(verticesCount) { Pos(0f, 0f) }
    private val vertices: FloatArray = FloatArray((verticesCount) * 3) { 0f }

    private fun updateVerticesPos() {
        var rotation = 0f
        verticesPos.forEach { vp ->
            vp.x = pos.x
            vp.y = pos.y + radius
            vp.rotateFromAnchor(pos, rotation)          // vertex rotation
            vp.rotateFromAnchor(pos, degreeRotation)    // shape rotation
            rotation += verticesDeg
        }
    }

    private fun updateVertices() {
        verticesPos.forEachIndexed { idx, vp ->
            vertices[idx * 3] = vp.x
            vertices[(idx * 3) + 1] = vp.y
        }
    }

    private fun createVertexBuffer(): FloatBuffer = ByteBuffer
        .allocateDirect(vertices.size * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer().apply {
            put(vertices)
            position(0)
        }

    override fun update() {
        updateVerticesPos()
        updateVertices()
    }

    override fun draw(gl: GL10) {
        //Set The Color
        gl.glColor4f(color[0], color[1], color[2], color[3])

        //Set the face rotation
        gl.glFrontFace(GL10.GL_CW)

        val vertexBuffer = createVertexBuffer()
        //Point to our vertex buffer
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer)

        //Enable vertex buffer
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)

        //Draw the vertices as triangle fan
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, vertices.size / 3)

        //Disable the client state before leaving
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)
    }
}