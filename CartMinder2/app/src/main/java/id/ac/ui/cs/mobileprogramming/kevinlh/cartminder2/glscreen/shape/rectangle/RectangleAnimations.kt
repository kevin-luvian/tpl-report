package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.shape.rectangle

import kotlin.math.max
import kotlin.math.min

interface RectangleAnimation {
    var animSpeed: Float
    fun update(rect: Rectangle)
}

class PullCycle(
    var minSize: Float = 1f,
    var maxSize: Float = 10f
) : RectangleAnimation {
    override var animSpeed: Float = 0.1f
    private var growFlag: Boolean = true
    private var directionFlag: Boolean = true

    override fun update(rect: Rectangle) {
        if (directionFlag) {
            if (growFlag) {
                if (rect.width <= maxSize) rect.width += animSpeed
                else growFlag = !growFlag
            } else {
                if (rect.width > minSize) rect.width -= animSpeed
                else {
                    directionFlag = !directionFlag
                    growFlag = !growFlag
                }
            }
        } else {
            if (growFlag) {
                if (rect.height <= maxSize) rect.height += animSpeed
                else growFlag = !growFlag
            } else {
                if (rect.height > minSize) rect.height -= animSpeed
                else {
                    directionFlag = !directionFlag
                    growFlag = !growFlag
                }
            }
        }
    }
}

class Rotation : RectangleAnimation {
    override var animSpeed: Float = 0.5f

    override fun update(rect: Rectangle) {
        rect.degreeRotation += animSpeed
    }
}

class Breathe(val initialSize: Float) : RectangleAnimation {
    override var animSpeed: Float = 0.5f
    var maxSize: Float = initialSize * 2f
    var animFlag = true

    override fun update(rect: Rectangle) {
        if (max(rect.width, rect.height) > maxSize ||
            min(rect.width, rect.height) < initialSize
        ) animFlag = !animFlag
        if (animFlag) {
            rect.width += animSpeed
            rect.height += animSpeed
        } else {
            rect.width -= animSpeed
            rect.height -= animSpeed
        }
    }
}