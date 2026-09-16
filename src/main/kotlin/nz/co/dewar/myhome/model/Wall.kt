package nz.co.dewar.myhome.model

import javafx.scene.shape.Polygon
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import nz.co.dewar.myhome.model.geom.Distance
import nz.co.dewar.myhome.model.geom.Point2D
import nz.co.dewar.myhome.model.geom.Vector2D
import nz.co.dewar.myhome.model.geom.metres
import nz.co.dewar.myhome.model.util.InheritedProperty

@Serializable
data class Wall(
    val id: String,
    val start: Point2D,
    val end: Point2D,
    val thickness: InheritedProperty<Distance> = InheritedProperty(0.1.metres),
    val openings: MutableList<Opening> = mutableListOf()
) : SelectableItem {
    init {
        openings.forEach { opening -> opening.wall = this }
    }

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
            val halfThicknessNormal = unitNormal * (thickness.value / 2)

            val extendedStart = start - (unitDirection * (thickness.value / 2))
            val extendedEnd = end + (unitDirection * (thickness.value / 2))

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
        val halfThicknessNormal = unitNormal * (thickness.value / 2)

        val openingStart = start + (unitDirection * opening.edgeDistanceFromWall)
        val openingEnd = openingStart + (unitDirection * opening.width)

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