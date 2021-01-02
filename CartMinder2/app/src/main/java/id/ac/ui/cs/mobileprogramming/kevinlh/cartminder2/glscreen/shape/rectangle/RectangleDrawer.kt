package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.shape.rectangle

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.shape.ShapeDrawer
import javax.microedition.khronos.opengles.GL10

class RectangleDrawer(val rectangle: Rectangle) : ShapeDrawer {
    private val animations: MutableList<RectangleAnimation> = mutableListOf()

    override fun draw(gl: GL10) {
        animations.forEach { it.update(rectangle) }
        rectangle.update()
        rectangle.draw(gl)
    }

    fun addPullCycleAnimation(animSpeed: Float? = null) = PullCycle().also { anim ->
        animSpeed?.let { anim.animSpeed = it }
        animations.add(anim)
    }

    fun addRotateAnimation(animSpeed: Float? = null) = Rotation().also { anim ->
        animSpeed?.let { anim.animSpeed = it }
        animations.add(anim)
    }

    fun addBreatheAnimation(animSpeed: Float? = null) = Breathe(rectangle.width).also { anim ->
        animSpeed?.let { anim.animSpeed = it }
        animations.add(anim)
    }
}