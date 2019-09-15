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

import net.imglib2.Interval
import net.imglib2.RandomAccessibleInterval
import net.imglib2.converter.Converters
import net.imglib2.img.array.ArrayImgs
import net.imglib2.type.logic.BitType
import net.imglib2.type.numeric.integer.IntType
import net.imglib2.type.numeric.real.DoubleType
import net.imglib2.type.numeric.real.FloatType
import net.imglib2.util.Intervals
import net.imglib2.view.Views
import org.junit.Assert
import org.junit.Test
import java.util.function.DoubleUnaryOperator
import java.util.function.Predicate
import kotlin.random.Random

class RandomAccessibleExtensionsKtTest {

    @Test
    fun cutoutAndBurnInTest() {
        val rng = Random(100L)
        val img = ArrayImgs.doubles(3L, 4L, 5L) as RandomAccessibleInterval<DoubleType>
        Assert.assertTrue(img.elementsEqual(0.0F).all())
        Assert.assertTrue(img.elementsEqual(0.0).all())
        Assert.assertTrue(img.elementsEqual(DoubleType(0.0)).all())
        img.flatIterable().forEach { it.set(rng.nextDouble()) }
        val cutoutInterval = Intervals.createMinMax(1, 3, 3, 2, 3, 4) as Interval
        val burntIn = img.burnIn(img)
        val cutout = img.burnIn(cutoutInterval)
        val copy = img.copy()

        Assert.assertTrue(img.elementsEqual(burntIn).all())
        Assert.assertTrue(Views.interval(img, cutout).elementsEqual(cutout).all())
        Assert.assertTrue(img.elementsEqual(copy).all())
    }

    @Test
    fun anyTest() {
        val img = ArrayImgs.ints(3L, 4L, 5L)
        Assert.assertTrue(img.elementsEqual(0).all())
        Assert.assertTrue(img.elementsEqual(0L).all())
        Assert.assertTrue(img.elementsEqual(IntType(0)).all())
        Assert.assertTrue(img.elementsEqual(0).any())
        Assert.assertFalse(img.elementsEqual(1).any())

        Assert.assertFalse(img.any(Predicate { it.integerLong == 1L }))
        Assert.assertTrue(img.any(Predicate { it.integerLong == 0L }))
        Assert.assertTrue(img.all(Predicate { it.integerLong == 0L }))


        img[1, 2, 3].set(1)
        Assert.assertFalse(img.elementsEqual(0).all())
        Assert.assertFalse(img.elementsEqual(0L).all())
        Assert.assertFalse(img.elementsEqual(IntType(0)).all())
        Assert.assertTrue(img.elementsEqual(0).any())
        Assert.assertTrue(img.elementsEqual(1).any())

        Assert.assertTrue(img.any(Predicate { it.integerLong == 1L }))
        Assert.assertTrue(img.any(Predicate { it.integerLong == 0L }))
        Assert.assertFalse(img.all(Predicate { it.integerLong == 0L }))
    }

    @Test
    fun plusTest() {
        val rng = Random(100L)
        val img = ArrayImgs.ints(1, 2, 3) as RandomAccessibleInterval<IntType>
        img.iterable().forEach { it.set(rng.nextInt(1, 10)) }
        Assert.assertFalse(img.elementsEqual(0).all())
        val sum1 = img.copy()
        sum1 += img
        val sum2 = img + img
        Assert.assertTrue(sum1.elementsEqual(sum2).all())
        Assert.assertFalse(sum1.elementsEqual(0).any())
        Assert.assertTrue(sum1.elementsEqual(Converters.convert(img, { s, t -> t.set(2*s.get())}, IntType())).all())

        ArrayImgs.ints(1, 2, 3).let { zeros ->
            Assert.assertTrue(zeros.elementsEqual(0).all())
            Assert.assertTrue((zeros + IntType(-1)).elementsEqual(-1).all())
            Assert.assertTrue((zeros + 1).elementsEqual(1).all())
            Assert.assertTrue((zeros + 2L).elementsEqual(2).all())
            Assert.assertTrue((IntType(-1) + zeros).elementsEqual(-1).all())
            Assert.assertTrue((1 + zeros).elementsEqual(1).all())
            Assert.assertTrue((2L + zeros).elementsEqual(2).all())

            zeros += 1
            Assert.assertTrue(zeros.elementsEqual(1).all())
            zeros += 2L
            Assert.assertTrue(zeros.elementsEqual(3).all())
            zeros += IntType(3)
            Assert.assertTrue(zeros.elementsEqual(6).all())

        }

        ArrayImgs.floats(1, 2, 3).let { zeros ->
            Assert.assertTrue(zeros.elementsEqual(0.0F).all())
            Assert.assertTrue((zeros + FloatType(-1.0F)).elementsEqual(-1.0).all())
            Assert.assertTrue((zeros + 1.0F).elementsEqual(1.0).all())
            Assert.assertTrue((zeros + 2.0).elementsEqual(2.0).all())
            Assert.assertTrue((FloatType(-1.0F) + zeros).elementsEqual(-1.0).all())
            Assert.assertTrue((1.0F + zeros).elementsEqual(1.0).all())
            Assert.assertTrue((2.0 + zeros).elementsEqual(2.0).all())

            zeros += 1.0
            Assert.assertTrue(zeros.elementsEqual(1.0).all())
            zeros += 2.0F
            Assert.assertTrue(zeros.elementsEqual(3.0).all())
            zeros += FloatType(3.0F)
            Assert.assertTrue(zeros.elementsEqual(6.0).all())
        }
    }

    @Test
    fun minusTest() {
        val rng = Random(100L)
        val img1 = ArrayImgs.ints(1, 2, 3) as RandomAccessibleInterval<IntType>
        val img2 = ArrayImgs.ints(1, 2, 3) as RandomAccessibleInterval<IntType>
        img1.iterable().forEach { it.set(rng.nextInt(1, 10)) }
        img2.iterable().forEach { it.set(rng.nextInt(1, 10)) }
        Assert.assertFalse(img1.elementsEqual(0).all())
        Assert.assertFalse(img2.elementsEqual(0).all())
        val diff1 = img1.copy()
        diff1 -= img2
        val diff2 = img1 - img2
        Assert.assertTrue(diff1.elementsEqual(diff2).all())
        Assert.assertTrue(diff1.elementsEqual(Views.interval(Converters.convert(Views.pair(img1, img2), { s, t -> t.set(s.a - s.b)}, IntType()), img1)).all())

        ArrayImgs.ints(1, 2, 3).let { zeros ->
            Assert.assertTrue(zeros.elementsEqual(0).all())
            Assert.assertTrue((zeros - IntType(-1)).elementsEqual(1).all())
            Assert.assertTrue((zeros - 1).elementsEqual(-1).all())
            Assert.assertTrue((zeros - 2L).elementsEqual(-2).all())
            Assert.assertTrue((IntType(-1) - zeros).elementsEqual(-1).all())
            Assert.assertTrue((1 - zeros).elementsEqual(1).all())
            Assert.assertTrue((2L - zeros).elementsEqual(2).all())

            zeros -= 1
            Assert.assertTrue(zeros.elementsEqual(-1).all())
            zeros -= 2L
            Assert.assertTrue(zeros.elementsEqual(-3).all())
            zeros -= IntType(3)
            Assert.assertTrue(zeros.elementsEqual(-6).all())
        }

        ArrayImgs.floats(1, 2, 3).let { zeros ->
            Assert.assertTrue(zeros.elementsEqual(0.0F).all())
            Assert.assertTrue((zeros - FloatType(-1.0F)).elementsEqual(1.0).all())
            Assert.assertTrue((zeros - 1.0F).elementsEqual(-1.0).all())
            Assert.assertTrue((zeros - 2.0).elementsEqual(-2.0).all())
            Assert.assertTrue((FloatType(-1.0F) - zeros).elementsEqual(-1.0).all())
            Assert.assertTrue((1.0F - zeros).elementsEqual(1.0).all())
            Assert.assertTrue((2.0 - zeros).elementsEqual(2.0).all())

            zeros -= 1.0
            Assert.assertTrue(zeros.elementsEqual(-1.0).all())
            zeros -= 2.0F
            Assert.assertTrue(zeros.elementsEqual(-3.0).all())
            zeros -= FloatType(3.0F)
            Assert.assertTrue(zeros.elementsEqual(-6.0).all())
        }
    }

    @Test
    fun timesTest() {
        val rng = Random(100L)
        val img = ArrayImgs.ints(1, 2, 3) as RandomAccessibleInterval<IntType>
        img.iterable().forEach { it.set(rng.nextInt(1, 10)) }
        Assert.assertFalse(img.elementsEqual(0).all())
        val sum1 = img.copy()
        sum1 *= img
        val sum2 = img * img
        Assert.assertTrue(sum1.elementsEqual(sum2).all())
        Assert.assertFalse(sum1.elementsEqual(0).any())
        Assert.assertTrue(sum1.elementsEqual(Converters.convert(img, { s, t -> t.set(s.get() * s.get())}, IntType())).all())

        ArrayImgs.ints(1, 2, 3).plus(1).let { ones ->
            Assert.assertTrue(ones.elementsEqual(1).all())
            Assert.assertTrue((ones * IntType(-1)).elementsEqual(-1).all())
            Assert.assertTrue((ones * 3).elementsEqual(3).all())
            Assert.assertTrue((ones * 2L).elementsEqual(2).all())
            Assert.assertTrue((IntType(-1) * ones).elementsEqual(-1).all())
            Assert.assertTrue((3 * ones).elementsEqual(3).all())
            Assert.assertTrue((2L * ones).elementsEqual(2).all())

            ones *= 1
            Assert.assertTrue(ones.elementsEqual(1).all())
            ones *= 2L
            Assert.assertTrue(ones.elementsEqual(2).all())
            ones *= IntType(3)
            Assert.assertTrue(ones.elementsEqual(6).all())
        }

        ArrayImgs.floats(1, 2, 3).plus(1.0F).let { ones ->
            Assert.assertTrue(ones.elementsEqual(1.0F).all())
            Assert.assertTrue((ones * FloatType(-1.0F)).elementsEqual(-1.0).all())
            Assert.assertTrue((ones * 3.0F).elementsEqual(3.0).all())
            Assert.assertTrue((ones * Math.PI).elementsEqual(Math.PI).all())
            Assert.assertTrue((FloatType(-1.0F) * ones).elementsEqual(-1.0).all())
            Assert.assertTrue((3F * ones).elementsEqual(3.0).all())
            Assert.assertTrue((Math.PI * ones).elementsEqual(Math.PI).all())

            ones *= 1.0
            Assert.assertTrue(ones.elementsEqual(1.0).all())
            ones *= 2.0F
            Assert.assertTrue(ones.elementsEqual(2.0).all())
            ones *= FloatType(3.0F)
            Assert.assertTrue(ones.elementsEqual(6.0).all())
        }
    }

    @Test
    fun divTest() {
        val rng = Random(100L)
        val img1 = ArrayImgs.ints(1, 2, 3) as RandomAccessibleInterval<IntType>
        val img2 = ArrayImgs.ints(1, 2, 3) as RandomAccessibleInterval<IntType>
        img1.iterable().forEach { it.set(rng.nextInt(1, 10)) }
        img2.iterable().forEach { it.set(rng.nextInt(1, 10)) }
        Assert.assertFalse(img1.elementsEqual(0).all())
        Assert.assertFalse(img2.elementsEqual(0).all())
        val diff1 = img1.copy()
        diff1 /= img2
        val diff2 = img1 / img2
        Assert.assertTrue(diff1.elementsEqual(diff2).all())
        Assert.assertTrue(diff1.elementsEqual(Views.interval(Converters.convert(Views.pair(img1, img2), { s, t -> t.set(s.a); t.divAssign(s.b)}, IntType()), img1)).all())

        ArrayImgs.ints(1, 2, 3).plus(10).let { tens ->
            Assert.assertTrue(tens.elementsEqual(10).all())
            Assert.assertTrue((tens / IntType(-1)).elementsEqual(-10).all())
            Assert.assertTrue((tens / 2).elementsEqual(5).all())
            Assert.assertTrue((tens / 3L).elementsEqual(3).all())
            Assert.assertTrue((IntType(-10) / tens).elementsEqual(-1).all())
            Assert.assertTrue((56 / tens).elementsEqual(5).all())
            Assert.assertTrue((30L / tens).elementsEqual(3).all())

            tens /= 2
            Assert.assertTrue(tens.elementsEqual(5).all())
            tens /= -3L
            Assert.assertTrue(tens.elementsEqual(-1).all())
            tens /= IntType(4)
            Assert.assertTrue(tens.elementsEqual(0).all())
        }

        ArrayImgs.floats(1, 2, 3).plus(1.0F).let { ones ->
            Assert.assertTrue(ones.elementsEqual(1.0F).all())
            Assert.assertTrue((ones / FloatType(-1.0F)).elementsEqual(-1.0).all())
            Assert.assertTrue((ones / 3.0F).elementsEqual(1.0 / 3.0).all())
            Assert.assertTrue((ones / 2.0).elementsEqual(0.5).all())
            Assert.assertTrue((FloatType(-1.0F) / ones).elementsEqual(-1.0).all())
            Assert.assertTrue((3.0F / ones).elementsEqual(3.0).all())
            Assert.assertTrue((2.0 / ones).elementsEqual(2.0).all())


            ones /= -2.0
            Assert.assertTrue(ones.elementsEqual(-0.5).all())
            ones /= 3.0F
            Assert.assertTrue(ones.elementsEqual(-0.5 / 3.0).all())
            ones /= FloatType(-4.0F)
            Assert.assertTrue(ones.elementsEqual(0.5 / 3.0 / 4.0).all())
        }
    }

    @Test
    fun unaryOpsTest() {
        val rng = Random(100L)
        val img = ArrayImgs.ints(1, 2, 3) as RandomAccessibleInterval<IntType>
        img.iterable().forEach { it.setInteger(rng.nextInt(1, 10).toLong()) }
        Assert.assertFalse(img.elementsEqual(0).any())
        Assert.assertTrue(Converters.convert(img, { s, t -> t.integer = +s.integer }, IntType()).elementsEqual(+img).all())
        Assert.assertTrue(Converters.convert(img, { s, t -> t.integer = -s.integer }, IntType()).elementsEqual(-img).all())
        val thresholded = Converters.convert(img, { s, t -> t.set(s.integer > 5) }, BitType())
        Assert.assertFalse(thresholded.elementsEqual(thresholded.not()).any())
    }

    @Test
    fun funcTest() {
        val rng = Random(100L)
        val X = DoubleArray(100, {rng.nextDouble()})
        val img = ArrayImgs.doubles(X, X.size.toLong())
        Assert.assertFalse(img.elementsEqual(0.0).all())
        Assert.assertArrayEquals(X.map { Math.exp(it) }.toDoubleArray(), img.exp().iterable().map { it.realDouble }.toDoubleArray(), 0.0)
        Assert.assertArrayEquals(X.map { Math.sqrt(it) }.toDoubleArray(), img.apply(DoubleUnaryOperator { Math.sqrt(it) }).iterable().map { it.realDouble }.toDoubleArray(), 0.0)
    }

}
