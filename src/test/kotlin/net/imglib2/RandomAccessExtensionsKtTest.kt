package net.imglib2

import net.imglib2.img.array.ArrayImgs
import net.imglib2.view.Views
import org.junit.Assert
import org.junit.Test
import kotlin.random.Random

class RandomAccessExtensionsKtTest {

    @Test
    fun getTest() {
        val rng = Random(100L)
        val img = ArrayImgs.doubles(1L, 2L, 3L)
        img.forEach { it.set(rng.nextDouble()) }
        val cursor = img.localizingCursor()
        val pos = LongArray(cursor.numDimensions())
        val intPos = IntArray(cursor.numDimensions())
        val extended = Views.extendZero(img)
        val access = img.randomAccess()

        while (cursor.hasNext()) {
            val expected = cursor.next()
            cursor.localize(pos)
            cursor.localize(intPos)

            Assert.assertEquals(expected, access[cursor])
            Assert.assertEquals(expected, access.get(*pos))
            Assert.assertEquals(expected, access.get(*intPos))

            Assert.assertEquals(expected, extended[cursor])
            Assert.assertEquals(expected, extended.get(*pos))
            Assert.assertEquals(expected, extended.get(*intPos))

            Assert.assertEquals(expected, extended[img, cursor])
            Assert.assertEquals(expected, extended.get(img, *pos))
            Assert.assertEquals(expected, extended.get(img, *intPos))
        }
    }

}