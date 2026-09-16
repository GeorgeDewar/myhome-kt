package nz.co.dewar.myhome.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import nz.co.dewar.myhome.model.geom.Distance
import nz.co.dewar.myhome.model.geom.sumOf
import nz.co.dewar.myhome.model.openingitem.OpeningItem
import nz.co.dewar.myhome.model.util.InheritedProperty

@Serializable
data class Opening(
    val id: String,
    val distanceAlongWall: Distance,
    val distanceFromFloor: Distance,
    val width: Distance,
    val height: Distance,
    val contents: MutableList<OpeningItem> = mutableListOf()
) : SelectableItem {
    @Transient
    lateinit var wall: Wall

    init {
        contents.forEach { item -> item.opening = this }
    }

    override val treeLabel = id

    val edgeDistanceFromWall: Distance
        get() = distanceAlongWall - (width / 2.0)

    init {
        // Resolve dimensions of contents
        val currentWidthSum = contents.sumOf { it.width.value }
        val currentHeightSum = contents.sumOf { it.height.value }

        check(currentWidthSum <= width) { "Total width of contents ($currentWidthSum) exceeds opening width ($width)" }
        check(currentHeightSum <= height) { "Total height of contents ($currentHeightSum) exceeds opening height ($height)" }

        if (currentWidthSum < width) {
            // Distribute remaining width among contents
            val remainingWidth = width - currentWidthSum
            val widthPerItem = remainingWidth / contents.size
            contents.forEach { item ->
                if (!item.width.isExplicit) {
                    item.width = InheritedProperty(widthPerItem)
                }
            }
        }

        if (currentHeightSum < height) {
            // Distribute remaining height among contents
            val remainingHeight = height - currentHeightSum
            val heightPerItem = remainingHeight / contents.size
            contents.forEach { item ->
                if (!item.height.isExplicit) {
                    item.height = InheritedProperty(heightPerItem)
                }
            }
        }

        contents.forEach { item ->
            // Placeholder for logic to determine posX and posY based on alignment and distribution rules
            if (!item.posX.isExplicit) {
                item.posX = InheritedProperty(Distance.ZERO)
            }
            if (!item.posY.isExplicit) {
                item.posY = InheritedProperty(Distance.ZERO)
            }
        }
    }

    override fun toString(): String {
        return "Opening(id='$id', distanceAlongWall=$distanceAlongWall, distanceFromFloor=$distanceFromFloor, width=$width, height=$height, ${contents.size} contents)"
    }
}