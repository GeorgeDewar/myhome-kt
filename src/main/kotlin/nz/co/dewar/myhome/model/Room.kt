package nz.co.dewar.myhome.model

import kotlinx.serialization.Serializable

@Serializable
data class Room(
    val name: String,
) : PlanItem {
    override val treeLabel = name
}