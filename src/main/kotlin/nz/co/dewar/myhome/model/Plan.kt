package nz.co.dewar.myhome.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import nz.co.dewar.myhome.model.openingitem.OpeningItem
import java.io.File

@Serializable
data class Plan(
    @SerialName($$"$schema")
    val schema: String? = null,
    val buildings: MutableList<Building> = mutableListOf()
) : PlanItem {
    override val treeLabel = "Plan"

    @Transient
    lateinit var fileName: String

    init {
        buildings.forEach { building -> building.plan = this }
    }

    fun getWallsOnLevel(level: Int): List<Wall> {
        return buildings.flatMap { building ->
            building.levels.find { it.number == level }?.walls ?: emptyList()
        }
    }

    fun getRoomsOnLevel(level: Int): List<Room> {
        return buildings.flatMap { building ->
            building.levels.find { it.number == level }?.rooms ?: emptyList()
        }
    }

    fun deleteItem(item: PlanItem) {
        when (item) {
            is Building -> buildings.remove(item)
            is Level -> item.building.levels.remove(item)
            is Wall -> item.level.walls.remove(item)
            is Opening -> item.wall.openings.remove(item)
            is OpeningItem -> item.opening.contents.remove(item)
            else -> throw IllegalArgumentException("Unknown PlanItem type")
        }
    }

    fun save() {
        val outputFile = File(fileName)
        outputFile.writeText(json.encodeToString(serializer(), this))
    }

    companion object {
        val json = Json { ignoreUnknownKeys = true }

        fun loadFromFile(fileName: String): Plan {
            val inputFile = File(fileName)
            val plan = json.decodeFromString<Plan>(inputFile.readText())
            plan.fileName = fileName
            return plan
        }
    }
}