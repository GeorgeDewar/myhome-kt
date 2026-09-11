package nz.co.dewar.myhome.model

import javafx.scene.shape.Polygon
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import nz.co.dewar.myhome.model.geom.Point2D
import nz.co.dewar.myhome.model.geom.Vector2D

@Serializable
data class Wall(
    val id: String,
    val start: Point2D,
    val end: Point2D,
    val thickness: Double = 0.1,
    val openings: List<Opening> = emptyList()
) : SelectableItem {
    override val treeLabel = id

    @Transient
    lateinit var level: Level

    @Transient
    val vector = Vector2D(start, end)

    @Transient
    val unitDirection = vector.unit()

    val angle: Double
        get() = vector.angle

    val areaPolygon: Polygon
        get() {
            val unitNormal = unitDirection.normal()
            val halfThicknessNormal = unitNormal * (thickness / 2.0)

            val extendedStart = start - (unitDirection * (thickness / 2.0))
            val extendedEnd = end + (unitDirection * (thickness / 2.0))

            val points = listOf(
                extendedStart + halfThicknessNormal,
                extendedEnd + halfThicknessNormal,
                extendedEnd - halfThicknessNormal,
                extendedStart - halfThicknessNormal
            )
            return Polygon(*points.flatMap { listOf(it.x, it.y) }.toDoubleArray())
        }

    fun openingPolygon(opening: Opening): Polygon {
        val unitNormal = unitDirection.normal()
        val halfThicknessNormal = unitNormal * (thickness / 2.0)

        val openingStart = start + (unitDirection * opening.edgeDistanceFromWall / 1000.0)
        val openingEnd = openingStart + (unitDirection * opening.width / 1000.0)

        val points = listOf(
            openingStart + halfThicknessNormal,
            openingEnd + halfThicknessNormal,
            openingEnd - halfThicknessNormal,
            openingStart - halfThicknessNormal
        )
        return Polygon(*points.flatMap { listOf(it.x, it.y) }.toDoubleArray())
    }

    override fun toString(): String {
        return "Wall(id='$id', start=$start, end=$end, thickness=$thickness, angle=$angle, ${openings.size} openings)"
    }
}