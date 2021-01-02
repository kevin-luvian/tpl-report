package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.shape.simplepolygon

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.shape.ShapeDrawer
import javax.microedition.khronos.opengles.GL10

class SimplePolygonDrawer(private val polygon: SimplePolygon) : ShapeDrawer {
    private val animations: MutableList<SimplePolygonAnimation> = mutableListOf()

    override fun draw(gl: GL10) {
        animations.forEach { it.update(polygon) }
        polygon.update()
        polygon.draw(gl)
    }

    fun addRotatingAnimation(animSpeed: Float = 0.1f) =
        Rotating(animSpeed).also { animations.add(it) }

    fun addBreathingAnimation(animSpeed: Float = 0.1f) =
        Breathing(animSpeed, polygon.radius).also { animations.add(it) }

    fun addMovingAnimation(animSpeed: Float = 0.1f) =
        Moving(animSpeed, polygon.pos).also { animations.add(it) }
}