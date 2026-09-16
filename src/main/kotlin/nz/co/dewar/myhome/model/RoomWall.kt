package nz.co.dewar.myhome.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class RoomWall(val ref: String) {
    @Transient
    lateinit var room: Room

    @Transient
    lateinit var wall: Wall
}