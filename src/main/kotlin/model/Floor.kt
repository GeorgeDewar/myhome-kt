package model

import kotlinx.serialization.Serializable

@Serializable
data class Floor(
    val number: Int,
    val name: String,
    val walls: List<Wall> = emptyList(),
    val rooms: List<Room> = emptyList()
) {
    init {
        walls.forEach { wall -> wall.floor = this }
    }
}