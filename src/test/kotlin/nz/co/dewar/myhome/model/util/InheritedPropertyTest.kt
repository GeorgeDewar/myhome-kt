package nz.co.dewar.myhome.model.util

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import nz.co.dewar.myhome.model.geom.Distance
import nz.co.dewar.myhome.model.geom.metres

class InheritedPropertyTest {
    private val json = Json { encodeDefaults = false }

    @Serializable
    private data class TestBox(
        val width: InheritedProperty<Distance> = InheritedProperty()
    )

    @Test
    fun `serializes explicit value using delegate serializer`() {
        val text = json.encodeToString(
            TestBox.serializer(),
            TestBox(width = InheritedProperty(1.2.metres, isExplicit = true))
        )

        assertEquals("""{"width":1200}""", text)
    }

    @Test
    fun `omits inherited value`() {
        val text = json.encodeToString(
            TestBox.serializer(),
            TestBox(width = InheritedProperty(1.2.metres, isExplicit = false))
        )

        assertEquals("""{}""", text)
    }

    @Test
    fun `deserializes present value as explicit`() {
        val box = json.decodeFromString<TestBox>("""{"width":1200}""")

        assertTrue(box.width.isExplicit)
        assertEquals(1.2, box.width.value!!.metres)
    }

    @Test
    fun `deserializes absent value as inherited`() {
        val box = json.decodeFromString<TestBox>("{}")

        assertEquals(InheritedProperty<Distance>(), box.width)
    }
}
