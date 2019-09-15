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

import net.imglib2.type.numeric.integer.ByteType
import net.imglib2.type.numeric.integer.IntType
import net.imglib2.type.numeric.integer.LongType
import net.imglib2.type.numeric.integer.ShortType
import net.imglib2.type.numeric.real.DoubleType
import org.junit.Assert
import org.junit.Test

class TypeExtensionsKtTest {

    @Test
    fun testAddReal() {
        val rt = DoubleType(1.0)
        Assert.assertEquals(DoubleType(2.0), rt + rt)
        Assert.assertEquals(DoubleType(2.0), rt + 1.0)
        Assert.assertEquals(DoubleType(3.0), rt + 2.0f)
        Assert.assertEquals(DoubleType(2.0), 1.0 + rt)
        Assert.assertEquals(DoubleType(3.0), 2.0f + rt)

        rt += rt
        Assert.assertEquals(DoubleType(2.0), rt)

        rt += 2.0
        Assert.assertEquals(DoubleType(4.0), rt)

        rt += 3.0F
        Assert.assertEquals(DoubleType(7.0), rt)
    }

    @Test
    fun testSubtractReal() {
        val rt = DoubleType(1.0)
        Assert.assertEquals(DoubleType(0.0), rt - rt)
        Assert.assertEquals(DoubleType(-1.0), rt - 2.0)
        Assert.assertEquals(DoubleType(-2.0), rt - 3.0f)
        Assert.assertEquals(DoubleType(1.0), 2.0 - rt)
        Assert.assertEquals(DoubleType(2.0), 3.0f - rt)

        rt -= rt
        Assert.assertEquals(DoubleType(0.0), rt)

        rt -= 2.0
        Assert.assertEquals(DoubleType(-2.0), rt)

        rt -= 3.0F
        Assert.assertEquals(DoubleType(-5.0), rt)
    }

    @Test
    fun testTimesReal() {
        val rt = DoubleType(1.0)
        Assert.assertEquals(DoubleType(1.0), rt * rt)
        Assert.assertEquals(DoubleType(2.0), rt * 2.0)
        Assert.assertEquals(DoubleType(3.0), rt * 3.0f)
        Assert.assertEquals(DoubleType(2.0), 2.0 * rt)
        Assert.assertEquals(DoubleType(3.0), 3.0f * rt)

        rt *= rt
        Assert.assertEquals(DoubleType(1.0), rt)

        rt *= 2.0
        Assert.assertEquals(DoubleType(2.0), rt)

        rt *= 3.0F
        Assert.assertEquals(DoubleType(6.0), rt)
    }

    @Test
    fun testDivReal() {
        val rt = DoubleType(1.0)
        // Cannot test this as it is shadowed by member RealType.div
//      Assert.assertEquals(DoubleType(1.0), rt / rt)
        Assert.assertEquals(DoubleType(1.0 / 2.0), rt / 2.0)
        Assert.assertEquals(DoubleType(1.0 / 3.0), rt / 3.0f)
        Assert.assertEquals(DoubleType(1.0 / 2.0), 2.0 / DoubleType(4.0))
        Assert.assertEquals(DoubleType(1.0 / 3.0), 3.0f / DoubleType(9.0))

        rt /= rt
        Assert.assertEquals(DoubleType(1.0), rt)

        rt /= 2.0
        Assert.assertEquals(DoubleType(1.0 / 2.0), rt)

        rt /= 3.0F
        Assert.assertEquals(DoubleType(1.0 / 2.0 / 3.0), rt)
    }

    @Test
    fun testAddInteger() {
        val lt = LongType(1L)
        Assert.assertEquals(LongType(2L), lt + lt)
        Assert.assertEquals(LongType(2L), lt + 1)
        Assert.assertEquals(LongType(3L), lt + 2L)
        Assert.assertEquals(LongType(2L), 1 + lt)
        Assert.assertEquals(LongType(3L), 2L + lt)

        lt += lt
        Assert.assertEquals(LongType(2L), lt)

        lt += 2
        Assert.assertEquals(LongType(4L), lt)

        lt += 3L
        Assert.assertEquals(LongType(7L), lt)
    }

    @Test
    fun testSubtractInteger() {
        val it = IntType(1)
        Assert.assertEquals(IntType(0), it - it)
        Assert.assertEquals(IntType(-1), it - 2)
        Assert.assertEquals(IntType(-2), it - 3L)
        Assert.assertEquals(IntType(1), 2 - it)
        Assert.assertEquals(IntType(2), 3L - it)

        it -= it
        Assert.assertEquals(IntType(0), it)

        it -= 2
        Assert.assertEquals(IntType(-2), it)

        it -= 3L
        Assert.assertEquals(IntType(-5), it)
    }

    @Test
    fun testTimesInteger() {
        val st = ShortType(1)
        Assert.assertEquals(ShortType(1), st * st)
        Assert.assertEquals(ShortType(2), st * 2)
        Assert.assertEquals(ShortType(3), st * 3L)
        Assert.assertEquals(ShortType(2), 2 * st)
        Assert.assertEquals(ShortType(3), 3L * st)

        st *= st
        Assert.assertEquals(ShortType(1), st)

        st *= 2
        Assert.assertEquals(ShortType(2), st)

        st *= 3L
        Assert.assertEquals(ShortType(6), st)
    }

    @Test
    fun testDivInteger() {
        val bt = ByteType(10)
        // Cannot test this as it is shadowed by member RealType.div
//      Assert.assertEquals(ByteType(1.0), rt / rt)
        Assert.assertEquals(ByteType(5), bt / 2)
        Assert.assertEquals(ByteType(3), bt / 3L)
        Assert.assertEquals(ByteType(5), 50 / bt)
        Assert.assertEquals(ByteType(3), 37L / bt)

        bt /= bt
        Assert.assertEquals(ByteType(1), bt)

        bt.set(10)
        bt /= 2
        Assert.assertEquals(ByteType(5), bt)

        bt /= 3L
        Assert.assertEquals(ByteType(1), bt)
    }

    @Test
    fun testUnaryPlusMinus() {
        Assert.assertEquals(DoubleType(1.0), +DoubleType(1.0))
        Assert.assertEquals(DoubleType(1.0), -DoubleType(-1.0))
        Assert.assertEquals(IntType(1), +IntType(1))
        Assert.assertEquals(IntType(1), -IntType(-1))
    }

}
