package nz.co.dewar.myhome.model.openingitem

import javafx.scene.Group
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nz.co.dewar.myhome.model.Opening
import nz.co.dewar.myhome.model.Wall

@Serializable
@SerialName("SlidingDoor")
class SlidingDoor(override val id: String? = null) : OpeningItem() {
    override val treeLabel = id ?: "Sliding Door"

    override fun render2D(wall: Wall, opening: Opening): Group {
        return Group()
    }
}