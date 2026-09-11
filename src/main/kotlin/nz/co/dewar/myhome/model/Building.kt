package nz.co.dewar.myhome.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Building(
    val name: String,
    val levels: List<Level>
) : PlanItem {
    @Transient
    lateinit var plan: Plan

    init {
        levels.forEach { level -> level.building = this }
    }

    override val treeLabel = name
}