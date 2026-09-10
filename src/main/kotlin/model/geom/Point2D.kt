package org.example.model.geom

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.IntArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.math.roundToInt

@Serializable(with = PositionSerializer::class)
data class Point2D(val x: Double, val y: Double) {
    operator fun plus(vector: Vector2D) = Point2D(x + vector.dX, y + vector.dY)
    operator fun minus(vector: Vector2D) = Point2D(x - vector.dX, y - vector.dY)
}

object PositionSerializer : KSerializer<Point2D> {
    private val delegateSerializer = IntArraySerializer()
    @OptIn(ExperimentalSerializationApi::class)
    override val descriptor = SerialDescriptor("position", delegateSerializer.descriptor)

    override fun serialize(encoder: Encoder, value: Point2D) {
        encoder.encodeSerializableValue(delegateSerializer, intArrayOf(value.x.roundToInt(), value.y.roundToInt()))
    }

    override fun deserialize(decoder: Decoder): Point2D {
        val array = decoder.decodeSerializableValue(delegateSerializer)
        return Point2D(array[0].toDouble() / 1000.0, array[1].toDouble() / 1000.0)
    }

}