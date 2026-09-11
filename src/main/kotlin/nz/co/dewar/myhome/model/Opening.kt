package nz.co.dewar.myhome.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import nz.co.dewar.myhome.model.openingitem.OpeningItem

enum class OpeningType {
    EMPTY, DOOR, WINDOW, COMPOSITE
}

@Serializable
data class Opening(
    val id: String,
    val distanceAlongWall: Double,
    val distanceFromFloor: Double,
    val width: Double,
    val height: Double,
    val contents: List<OpeningItem> = emptyList()
) : SelectableItem {
    @Transient
    lateinit var wall: Wall

    init {
        contents.forEach { item -> item.opening = this }
    }

    override val treeLabel = id

    val edgeDistanceFromWall: Double
        get() = distanceAlongWall - (width / 2)

    val type: OpeningType
        get() = when {
            contents.isEmpty() -> OpeningType.EMPTY
            contents.all { it is nz.co.dewar.myhome.model.openingitem.StandardDoor } -> OpeningType.DOOR
            contents.all { it is nz.co.dewar.myhome.model.openingitem.StandardWindow } -> OpeningType.WINDOW
            else -> OpeningType.COMPOSITE
        }

    init {
        // Resolve dimensions of contents
        val currentWidthSum = contents.sumOf { it.width ?: 0.0 }
        val currentHeightSum = contents.sumOf { it.height ?: 0.0 }

        check(currentWidthSum <= width) { "Total width of contents ($currentWidthSum) exceeds opening width ($width)" }
        check(currentHeightSum <= height) { "Total height of contents ($currentHeightSum) exceeds opening height ($height)" }

        if (currentWidthSum < width) {
            // Distribute remaining width among contents
            val remainingWidth = width - currentWidthSum
            val widthPerItem = remainingWidth / contents.size
            contents.forEach { item ->
                if (item.width == null) {
                    item.width = widthPerItem
                }
            }
        }

        if (currentHeightSum < height) {
            // Distribute remaining height among contents
            val remainingHeight = height - currentHeightSum
            val heightPerItem = remainingHeight / contents.size
            contents.forEach { item ->
                if (item.height == null) {
                    item.height = heightPerItem
                }
            }
        }

        contents.forEach { item ->
            // Placeholder for logic to determine posX and posY based on alignment and distribution rules
            if (item.posX == null) {
                item.posX = 0.0
            }
            if (item.posY == null) {
                item.posY = 0.0
            }
        }
    }

    override fun toString(): String {
        return "Opening(id='$id', distanceAlongWall=$distanceAlongWall, distanceFromFloor=$distanceFromFloor, width=$width, height=$height, ${contents.size} contents)"
    }
}