package nz.co.dewar.myhome.model.geom

import kotlin.math.abs

data class Line2D(val start: Point2D, val end: Point2D) {
    val vector get() = Vector2D(start, end)

    /**
     * Returns the point where this and [other] finite lines intersect, or `null` when they
     * do not.
     */
    fun intersection(other: Line2D): Point2D? {
        val thisStart = start
        val otherStart = other.start
        val denominator = vector.cross(other.vector)
        if (abs(denominator) < INTERSECTION_EPSILON) {
            return collinearIntersection(other)
        }

        val startDifference = Vector2D(thisStart, otherStart)
        val thisFraction = startDifference.cross(other.vector) / denominator
        val otherFraction = startDifference.cross(this.vector) / denominator
        if (
            thisFraction < -INTERSECTION_EPSILON || thisFraction > 1.0 + INTERSECTION_EPSILON ||
            otherFraction < -INTERSECTION_EPSILON || otherFraction > 1.0 + INTERSECTION_EPSILON
        ) {
            return null
        }

        return thisStart + this.vector * thisFraction.coerceIn(0.0, 1.0)
    }

    private fun collinearIntersection(other: Line2D): Point2D? {
        if (abs(Vector2D(start, other.start).cross(vector)) >= INTERSECTION_EPSILON) {
            return null
        }

        return listOf(start, end, other.start, other.end)
            .distinct()
            .filter { it.isOn(this) && it.isOn(other) }
            .singleOrNull()
    }

    private fun Point2D.isOn(line: Line2D): Boolean {
        val lineVector = line.vector
        val pointVector = Vector2D(line.start, this)
        return abs(pointVector.cross(lineVector)) < INTERSECTION_EPSILON &&
                pointVector.dX * lineVector.dX + pointVector.dY * lineVector.dY >= -INTERSECTION_EPSILON &&
                pointVector.dX * lineVector.dX + pointVector.dY * lineVector.dY <=
                lineVector.dX * lineVector.dX + lineVector.dY * lineVector.dY + INTERSECTION_EPSILON
    }

    private companion object {
        const val INTERSECTION_EPSILON = 1e-10
    }
}