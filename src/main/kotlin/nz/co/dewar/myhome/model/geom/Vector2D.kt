package nz.co.dewar.myhome.model.geom

import java.lang.Math.toDegrees
import kotlin.math.atan2
import kotlin.math.sqrt

data class Vector2D(val dX: Double, val dY: Double) {
    constructor(start: Point2D, end: Point2D) : this(end.x - start.x, end.y - start.y)

    val length
        get() = sqrt(dX * dX + dY * dY)

    /** The angle of the vector, in degrees, measured counter-clockwise from the positive x-axis. */
    val angle
        get() = toDegrees(atan2(-dY, dX)).mod(360.0)

    operator fun plus(v: Vector2D) = Vector2D(dX + v.dX, dY + v.dY)
    operator fun minus(v: Vector2D) = Vector2D(dX - v.dX, dY - v.dY)
    operator fun times(n: Double) = Vector2D(dX * n, dY * n)
    operator fun div(n: Double) = Vector2D(dX / n, dY / n)

    fun unit() = this / length

    fun normal() = Vector2D(-dY, dX)

    override fun toString(): String {
        return "Vector($dX, $dY; l = $length)"
    }
}