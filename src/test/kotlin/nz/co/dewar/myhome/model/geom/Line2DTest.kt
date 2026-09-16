package nz.co.dewar.myhome.model.geom

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class Line2DTest {
    @Test
    fun `returns intersection of crossing segments`() {
        val intersection = Line2D(Point2D(0.0, 0.0), Point2D(4.0, 4.0))
            .intersection(Line2D(Point2D(0.0, 4.0), Point2D(4.0, 0.0)))

        assertEquals(Point2D(2.0, 2.0), intersection)
    }

    @Test
    fun `returns intersection at shared endpoint`() {
        val intersection = Line2D(Point2D(0.0, 0.0), Point2D(2.0, 0.0))
            .intersection(Line2D(Point2D(2.0, 0.0), Point2D(2.0, 3.0)))

        assertEquals(Point2D(2.0, 0.0), intersection)
    }

    @Test
    fun `returns intersection where collinear segments touch`() {
        val intersection = Line2D(Point2D(0.0, 0.0), Point2D(2.0, 0.0))
            .intersection(Line2D(Point2D(2.0, 0.0), Point2D(5.0, 0.0)))

        assertEquals(Point2D(2.0, 0.0), intersection)
    }

    @Test
    fun `returns null for parallel or overlapping collinear segments`() {
        assertNull(
            Line2D(Point2D(0.0, 0.0), Point2D(2.0, 0.0))
                .intersection(Line2D(Point2D(0.0, 1.0), Point2D(2.0, 1.0)))
        )
        assertNull(
            Line2D(Point2D(0.0, 0.0), Point2D(2.0, 0.0))
                .intersection(Line2D(Point2D(1.0, 0.0), Point2D(3.0, 0.0)))
        )
    }

    @Test
    fun `returns null when line intersection lies outside either segment`() {
        val intersection = Line2D(Point2D(0.0, 0.0), Point2D(1.0, 0.0))
            .intersection(Line2D(Point2D(2.0, -1.0), Point2D(2.0, 1.0)))

        assertNull(intersection)
    }
}
