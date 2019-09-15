/*-
 * #%L
 * Kotlin extensions for imglib2
 * %%
 * Copyright (C) 2019 Philipp Hanslovsky
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package net.imglib2.imklib.extensions

import net.imglib2.Point
import net.imglib2.imklib.extensions.get
import net.imglib2.imklib.extensions.maxAsInts
import net.imglib2.imklib.extensions.maxAsLongs
import net.imglib2.imklib.extensions.maxAsPoint
import net.imglib2.imklib.extensions.minAsInts
import net.imglib2.imklib.extensions.minAsLongs
import net.imglib2.imklib.extensions.minAsPoint
import net.imglib2.img.array.ArrayImgs
import net.imglib2.util.Intervals
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

    @Test
    fun minMaxTest() {
        val img = Views.translate(ArrayImgs.doubles(1, 2, 3), 1, 2, 3)
        Assert.assertArrayEquals(Intervals.minAsLongArray(img), img.minAsLongs())
        Assert.assertArrayEquals(Intervals.maxAsLongArray(img), img.maxAsLongs())
        Assert.assertArrayEquals(Intervals.minAsIntArray(img), img.minAsInts())
        Assert.assertArrayEquals(Intervals.maxAsIntArray(img), img.maxAsInts())
        Assert.assertEquals(Point(*Intervals.minAsLongArray(img)), img.minAsPoint())
        Assert.assertEquals(Point(*Intervals.maxAsLongArray(img)), img.maxAsPoint())
    }

}
