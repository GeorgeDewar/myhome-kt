package nz.co.dewar.myhome.model.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = InheritedPropertySerializer::class)
data class InheritedProperty<T>(val value: T, val isExplicit: Boolean = false) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is InheritedProperty<*>) return false
        if (isExplicit != other.isExplicit) return false
        return !isExplicit || value == other.value
    }

    override fun hashCode(): Int {
        return if (isExplicit) {
            31 * (value?.hashCode() ?: 0) + 1
        } else {
            0
        }
    }

    override fun toString(): String {
        return if (isExplicit) {
            "InheritedProperty.explicit($value)"
        } else {
            "InheritedProperty.inherited($value)"
        }
    }
}

class InheritedPropertySerializer<T>(private val valueSerializer: KSerializer<T>) : KSerializer<InheritedProperty<T>> {
    override val descriptor: SerialDescriptor = valueSerializer.descriptor

    override fun serialize(encoder: Encoder, value: InheritedProperty<T>) {
        if (!value.isExplicit) {
            throw SerializationException(
                "InheritedProperty with isExplicit=false must be omitted by the owning serializer"
            )
        }

        val explicitValue = value.value
            ?: throw SerializationException("InheritedProperty with isExplicit=true must have a value")

        encoder.encodeSerializableValue(valueSerializer, explicitValue)
    }

    override fun deserialize(decoder: Decoder): InheritedProperty<T> {
        return InheritedProperty(
            value = decoder.decodeSerializableValue(valueSerializer),
            isExplicit = true
        )
    }
}
