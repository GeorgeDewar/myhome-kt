package nz.co.dewar.myhome.model.openingitem

import javafx.scene.Group
import javafx.scene.paint.Color
import javafx.scene.shape.Arc
import javafx.scene.transform.Rotate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nz.co.dewar.myhome.graphics2d.Polygon
import nz.co.dewar.myhome.model.Opening
import nz.co.dewar.myhome.model.Wall

enum class HingeSide {
    @SerialName("left")
    LEFT,

    @SerialName("right")
    RIGHT
}

enum class SwingDirection {
    @SerialName("in")
    INWARD,

    @SerialName("out")
    OUTWARD
}

@Serializable
@SerialName("StandardDoor")
class StandardDoor(
    override val id: String? = null,
    val hingeSide: HingeSide,
    val swingDirection: SwingDirection,
    val thickness: Double = 0.035,
) : OpeningItem() {
    override val treeLabel = id ?: "Standard Door"

    override fun render2D(wall: Wall, opening: Opening): Group {
        val relativeOpenAngle = 90.0
        val doorStart = wall.start + wall.unitDirection * (opening.edgeDistanceFromWall + posX!!) / 1000.0
        val doorEnd = doorStart + wall.unitDirection * width!! / 1000.0
        val hingePosCentre = if (hingeSide == HingeSide.LEFT) doorStart else doorEnd
        val hingePosNormal =
            wall.unitDirection.normal() * wall.thickness * (if (swingDirection == SwingDirection.INWARD) 0.5 else -0.5)
        val hingePos = hingePosCentre + hingePosNormal

        val closedAngle = if (hingeSide == HingeSide.LEFT) wall.angle else (wall.angle + 180).mod(360.0)
        val sweepAngle =
            if (hingeSide == HingeSide.LEFT && swingDirection == SwingDirection.INWARD || hingeSide == HingeSide.RIGHT && swingDirection == SwingDirection.OUTWARD) -relativeOpenAngle else relativeOpenAngle

        val closedDirection = if (hingeSide == HingeSide.LEFT) wall.unitDirection else wall.unitDirection * -1.0
        // Leaf rectangle is initially drawn in the closed position, then rotated around the hinge point to simulate opening
        val doorLeaf = Polygon(
            hingePos,
            hingePos + closedDirection * width!! / 1000.0,
            hingePos + closedDirection * width!! / 1000.0 - wall.unitDirection.normal() * thickness,
            hingePos - wall.unitDirection.normal() * thickness
        )
        doorLeaf.fill = null
        doorLeaf.stroke = Color.BLACK
        doorLeaf.strokeWidth = 0.01

        val doorLeafRotation = Rotate(-sweepAngle, hingePos.x, hingePos.y)
        doorLeaf.transforms.add(doorLeafRotation)

        val doorArc = Arc()
        doorArc.centerX = hingePos.x
        doorArc.centerY = hingePos.y
        doorArc.radiusX = width!! / 1000.0
        doorArc.radiusY = width!! / 1000.0
        doorArc.startAngle = closedAngle
        doorArc.length = sweepAngle
        doorArc.fill = null
        doorArc.stroke = Color.BLACK
        doorArc.strokeWidth = 0.01

        return Group(doorLeaf, doorArc)
    }

    override fun toString(): String {
        return "StandardDoor(id=$id, hingeSide=$hingeSide, swingDirection=$swingDirection, thickness=$thickness, width=$width, height=$height, posX=$posX, posY=$posY)"
    }
}