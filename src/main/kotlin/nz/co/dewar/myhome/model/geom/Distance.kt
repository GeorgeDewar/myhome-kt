package nz.co.dewar.myhome.model.geom

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.math.roundToInt

/** A distance measured in metres, serialized in integer millimetres */
@Serializable(with = DistanceSerializer::class)
class Distance(val metres: Double) {
    operator fun plus(other: Distance) = Distance(metres + other.metres)
    operator fun minus(other: Distance) = Distance(metres - other.metres)
    operator fun times(factor: Double) = Distance(metres * factor)
    operator fun div(factor: Double) = Distance(metres / factor)
    operator fun div(factor: Int) = Distance(metres / factor.toDouble())
    operator fun compareTo(other: Distance): Int = metres.compareTo(other.metres)

    companion object {
        val ZERO = Distance(0.0)
    }
}

val Number.metres get() = Distance(this.toDouble())

inline fun <T> Iterable<T>.sumOf(selector: (T) -> Distance): Distance {
    return this.fold(Distance.ZERO) { acc, item -> acc + selector(item) }
}

object DistanceSerializer : KSerializer<Distance> {
    @OptIn(ExperimentalSerializationApi::class)
    override val descriptor = PrimitiveSerialDescriptor(Distance::class.qualifiedName!!, PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: Distance) {
        encoder.encodeInt((value.metres * 1000).roundToInt())
    }

    override fun deserialize(decoder: Decoder): Distance {
        return Distance(metres = decoder.decodeInt().toDouble() / 1000.0)
    }
}