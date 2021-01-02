package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.shape

import javax.microedition.khronos.opengles.GL10

interface Shape {
    var pos: Pos
    var degreeRotation: Float
    var color: FloatArray

    fun update()
    fun draw(gl: GL10)
}