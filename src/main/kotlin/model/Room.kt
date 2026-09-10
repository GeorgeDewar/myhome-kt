package model

import kotlinx.serialization.Serializable

@Serializable
data class Room(
    val name: String,
) {
}