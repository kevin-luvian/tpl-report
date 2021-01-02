package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.shape.simplepolygon

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.shape.Pos
import kotlin.math.abs

interface SimplePolygonAnimation {
    var animSpeed: Float
    fun update(polygon: SimplePolygon)
}

class Rotating(override var animSpeed: Float) : SimplePolygonAnimation {
    override fun update(polygon: SimplePolygon) {
        polygon.degreeRotation += animSpeed
    }
}

class Breathing(
    override var animSpeed: Float,
    private val initialSize: Float,
) : SimplePolygonAnimation {
    var maxSize: Float = initialSize * 2f
    private var animFlag = true

    override fun update(polygon: SimplePolygon) {
        if (polygon.radius > maxSize || polygon.radius < initialSize)
            animFlag = !animFlag
        if (animFlag) polygon.radius += animSpeed
        else polygon.radius -= animSpeed
    }
}

class Moving(
    override var animSpeed: Float,
    private val initialPos: Pos
) : SimplePolygonAnimation {
    var paths: MutableList<Pos> = mutableListOf(initialPos.copy())
    var indexMoveTo: Int = 0

    fun addPath(x: Float, y: Float) {
        paths.add(Pos(x + initialPos.x, y + initialPos.y))
    }

    override fun update(polygon: SimplePolygon) {
        val diffPos = paths[indexMoveTo].copy().sub(polygon.pos)
        if (abs(diffPos.x) < animSpeed && abs(diffPos.y) < animSpeed) {
            polygon.pos = paths[indexMoveTo].copy()
            if (++indexMoveTo >= paths.size) indexMoveTo = 0
        }
        val drawPos = calcPosNormalized(diffPos)
        polygon.pos.add(drawPos)
    }

    private fun calcPosNormalized(pos: Pos): Pos {
        return Pos(normalizeToSpeed(pos.x), normalizeToSpeed(pos.y))
    }

    private fun normalizeToSpeed(f: Float): Float {
        if (f > 0f) return animSpeed
        else if (f < 0f) return -animSpeed
        return 0f
    }
}