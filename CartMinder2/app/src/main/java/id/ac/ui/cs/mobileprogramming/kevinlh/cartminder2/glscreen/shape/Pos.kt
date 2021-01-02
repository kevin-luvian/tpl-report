package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.shape

import kotlin.math.abs

data class Pos(var x: Float, var y: Float) {
    // load the native library on initialization of this class
    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    private external fun add2dArray(pos: FloatArray, otherPos: FloatArray): FloatArray
    private external fun sub2dArray(pos: FloatArray, otherPos: FloatArray): FloatArray
    private external fun rotate2dArray(pos: FloatArray, deg: Float): FloatArray

    fun copy(): Pos = Pos(x, y)

    fun toFloatArray(): FloatArray = floatArrayOf(x, y)

    fun add(other: Pos): Pos {
        val res = add2dArray(toFloatArray(), other.toFloatArray())
        x = res[0]
        y = res[1]
        return this
    }

    fun sub(other: Pos): Pos {
        val res = sub2dArray(toFloatArray(), other.toFloatArray())
        x = res[0]
        y = res[1]
        return this
    }

    fun rotateFromAnchor(anchor: Pos, deg: Float) = sub(anchor).rotate(deg).add(anchor)

    private fun rotate(deg: Float): Pos {
        val res = rotate2dArray(toFloatArray(), deg)
        x = if (abs(res[0]) <= 0.001) 0f else res[0]
        y = if (abs(res[1]) <= 0.001) 0f else res[1]
        return this
    }
}