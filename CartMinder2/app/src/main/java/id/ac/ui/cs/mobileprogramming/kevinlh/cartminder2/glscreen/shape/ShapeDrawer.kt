package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.shape

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.shape.rectangle.Rectangle
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.shape.rectangle.RectangleDrawer
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.shape.simplepolygon.SimplePolygon
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.shape.simplepolygon.SimplePolygonDrawer
import javax.microedition.khronos.opengles.GL10

interface ShapeDrawer {
    fun draw(gl: GL10)

    companion object {
        fun getShapes(): List<ShapeDrawer> = listOf(
            RectangleDrawer(Rectangle()
                .apply {
                    setPos(22f, 25f)
                    width = 5f
                    height = 5f
                    color = Colors.YELLOW
                })
                .apply {
                    addPullCycleAnimation().apply {
                        animSpeed = 0.5f
                        minSize = 5f
                        maxSize = 50f
                    }
                },
            RectangleDrawer(Rectangle()
                .apply {
                    degreeRotation = 90f
                    width = 3f
                    height = 3f
                    color = Colors.DARKGRAY
                })
                .apply {
                    addPullCycleAnimation(0.05f).apply {
                        minSize = 3f
                        maxSize = 50f
                    }
                    addRotateAnimation(0.3f)
                },
            SimplePolygonDrawer(SimplePolygon(Pos(-11f, -30f), 3, 10f)
                .apply { color = Colors.CRIMSON })
                .apply {
                    addRotatingAnimation(-0.5f)
                },
            SimplePolygonDrawer(SimplePolygon(Pos(-17f, 40f), 6, 4f)
                .apply { color = Colors.GREEN })
                .apply {
                    addMovingAnimation().apply {
                        addPath(0f, -65f)
                    }
                    addBreathingAnimation(0.005f).apply { maxSize = 7f }
                    addRotatingAnimation(-0.5f)
                },
            SimplePolygonDrawer(SimplePolygon(Pos(0f, 0f), 50, 4f))
                .apply {
                    addBreathingAnimation(0.01f).apply { maxSize = 6f }
                }
        )
    }
}