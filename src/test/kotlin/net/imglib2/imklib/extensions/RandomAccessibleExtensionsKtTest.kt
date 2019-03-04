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
        Assert.assertTrue(img.contentsEqual(0.0F).all())
        Assert.assertTrue(img.contentsEqual(0.0).all())
        Assert.assertTrue(img.contentsEqual(DoubleType(0.0)).all())
        img.flatIterable().forEach { it.set(rng.nextDouble()) }
        val cutoutInterval = Intervals.createMinMax(1, 3, 3, 2, 3, 4) as Interval
        val burntIn = img.burnIn(img)
        val cutout = img.burnIn(cutoutInterval)
        val copy = img.copy()

        Assert.assertTrue(img.contentsEqual(burntIn).all())
        Assert.assertTrue(Views.interval(img, cutout).contentsEqual(cutout).all())
        Assert.assertTrue(img.contentsEqual(copy).all())
    }

    @Test
    fun anyTest() {
        val img = ArrayImgs.ints(3L, 4L, 5L)
        Assert.assertTrue(img.contentsEqual(0).all())
        Assert.assertTrue(img.contentsEqual(0L).all())
        Assert.assertTrue(img.contentsEqual(IntType(0)).all())
        Assert.assertTrue(img.contentsEqual(0).any())
        Assert.assertFalse(img.contentsEqual(1).any())

        Assert.assertFalse(img.any(Predicate { it.integerLong == 1L }))
        Assert.assertTrue(img.any(Predicate { it.integerLong == 0L }))
        Assert.assertTrue(img.all(Predicate { it.integerLong == 0L }))


        img[1, 2, 3].set(1)
        Assert.assertFalse(img.contentsEqual(0).all())
        Assert.assertFalse(img.contentsEqual(0L).all())
        Assert.assertFalse(img.contentsEqual(IntType(0)).all())
        Assert.assertTrue(img.contentsEqual(0).any())
        Assert.assertTrue(img.contentsEqual(1).any())

        Assert.assertTrue(img.any(Predicate { it.integerLong == 1L }))
        Assert.assertTrue(img.any(Predicate { it.integerLong == 0L }))
        Assert.assertFalse(img.all(Predicate { it.integerLong == 0L }))
    }

    @Test
    fun plusTest() {
        val rng = Random(100L)
        val img = ArrayImgs.ints(1, 2, 3) as RandomAccessibleInterval<IntType>
        img.iterable().forEach { it.set(rng.nextInt(1, 10)) }
        Assert.assertFalse(img.contentsEqual(0).all())
        val sum1 = img.copy()
        sum1 += img
        val sum2 = img + img
        Assert.assertTrue(sum1.contentsEqual(sum2).all())
        Assert.assertFalse(sum1.contentsEqual(0).any())
        Assert.assertTrue(sum1.contentsEqual(Converters.convert(img, {s, t -> t.set(2*s.get())}, IntType())).all())

        ArrayImgs.ints(1, 2, 3).let { zeros ->
            Assert.assertTrue(zeros.contentsEqual(0).all())
            Assert.assertTrue((zeros + IntType(-1)).contentsEqual(-1).all())
            Assert.assertTrue((zeros + 1).contentsEqual(1).all())
            Assert.assertTrue((zeros + 2L).contentsEqual(2).all())
            Assert.assertTrue((IntType(-1) + zeros).contentsEqual(-1).all())
            Assert.assertTrue((1 + zeros).contentsEqual(1).all())
            Assert.assertTrue((2L + zeros).contentsEqual(2).all())

            zeros += 1
            Assert.assertTrue(zeros.contentsEqual(1).all())
            zeros += 2L
            Assert.assertTrue(zeros.contentsEqual(3).all())
            zeros += IntType(3)
            Assert.assertTrue(zeros.contentsEqual(6).all())

        }

        ArrayImgs.floats(1, 2, 3).let { zeros ->
            Assert.assertTrue(zeros.contentsEqual(0.0F).all())
            Assert.assertTrue((zeros + FloatType(-1.0F)).contentsEqual(-1.0).all())
            Assert.assertTrue((zeros + 1.0F).contentsEqual(1.0).all())
            Assert.assertTrue((zeros + 2.0).contentsEqual(2.0).all())
            Assert.assertTrue((FloatType(-1.0F) + zeros).contentsEqual(-1.0).all())
            Assert.assertTrue((1.0F + zeros).contentsEqual(1.0).all())
            Assert.assertTrue((2.0 + zeros).contentsEqual(2.0).all())

            zeros += 1.0
            Assert.assertTrue(zeros.contentsEqual(1.0).all())
            zeros += 2.0F
            Assert.assertTrue(zeros.contentsEqual(3.0).all())
            zeros += FloatType(3.0F)
            Assert.assertTrue(zeros.contentsEqual(6.0).all())
        }
    }

    @Test
    fun minusTest() {
        val rng = Random(100L)
        val img1 = ArrayImgs.ints(1, 2, 3) as RandomAccessibleInterval<IntType>
        val img2 = ArrayImgs.ints(1, 2, 3) as RandomAccessibleInterval<IntType>
        img1.iterable().forEach { it.set(rng.nextInt(1, 10)) }
        img2.iterable().forEach { it.set(rng.nextInt(1, 10)) }
        Assert.assertFalse(img1.contentsEqual(0).all())
        Assert.assertFalse(img2.contentsEqual(0).all())
        val diff1 = img1.copy()
        diff1 -= img2
        val diff2 = img1 - img2
        Assert.assertTrue(diff1.contentsEqual(diff2).all())
        Assert.assertTrue(diff1.contentsEqual(Views.interval(Converters.convert(Views.pair(img1, img2), { s, t -> t.set(s.a - s.b)}, IntType()), img1)).all())

        ArrayImgs.ints(1, 2, 3).let { zeros ->
            Assert.assertTrue(zeros.contentsEqual(0).all())
            Assert.assertTrue((zeros - IntType(-1)).contentsEqual(1).all())
            Assert.assertTrue((zeros - 1).contentsEqual(-1).all())
            Assert.assertTrue((zeros - 2L).contentsEqual(-2).all())
            Assert.assertTrue((IntType(-1) - zeros).contentsEqual(-1).all())
            Assert.assertTrue((1 - zeros).contentsEqual(1).all())
            Assert.assertTrue((2L - zeros).contentsEqual(2).all())

            zeros -= 1
            Assert.assertTrue(zeros.contentsEqual(-1).all())
            zeros -= 2L
            Assert.assertTrue(zeros.contentsEqual(-3).all())
            zeros -= IntType(3)
            Assert.assertTrue(zeros.contentsEqual(-6).all())
        }

        ArrayImgs.floats(1, 2, 3).let { zeros ->
            Assert.assertTrue(zeros.contentsEqual(0.0F).all())
            Assert.assertTrue((zeros - FloatType(-1.0F)).contentsEqual(1.0).all())
            Assert.assertTrue((zeros - 1.0F).contentsEqual(-1.0).all())
            Assert.assertTrue((zeros - 2.0).contentsEqual(-2.0).all())
            Assert.assertTrue((FloatType(-1.0F) - zeros).contentsEqual(-1.0).all())
            Assert.assertTrue((1.0F - zeros).contentsEqual(1.0).all())
            Assert.assertTrue((2.0 - zeros).contentsEqual(2.0).all())

            zeros -= 1.0
            Assert.assertTrue(zeros.contentsEqual(-1.0).all())
            zeros -= 2.0F
            Assert.assertTrue(zeros.contentsEqual(-3.0).all())
            zeros -= FloatType(3.0F)
            Assert.assertTrue(zeros.contentsEqual(-6.0).all())
        }
    }

    @Test
    fun timesTest() {
        val rng = Random(100L)
        val img = ArrayImgs.ints(1, 2, 3) as RandomAccessibleInterval<IntType>
        img.iterable().forEach { it.set(rng.nextInt(1, 10)) }
        Assert.assertFalse(img.contentsEqual(0).all())
        val sum1 = img.copy()
        sum1 *= img
        val sum2 = img * img
        Assert.assertTrue(sum1.contentsEqual(sum2).all())
        Assert.assertFalse(sum1.contentsEqual(0).any())
        Assert.assertTrue(sum1.contentsEqual(Converters.convert(img, {s, t -> t.set(s.get() * s.get())}, IntType())).all())

        ArrayImgs.ints(1, 2, 3).plus(1).let { ones ->
            Assert.assertTrue(ones.contentsEqual(1).all())
            Assert.assertTrue((ones * IntType(-1)).contentsEqual(-1).all())
            Assert.assertTrue((ones * 3).contentsEqual(3).all())
            Assert.assertTrue((ones * 2L).contentsEqual(2).all())
            Assert.assertTrue((IntType(-1) * ones).contentsEqual(-1).all())
            Assert.assertTrue((3 * ones).contentsEqual(3).all())
            Assert.assertTrue((2L * ones).contentsEqual(2).all())

            ones *= 1
            Assert.assertTrue(ones.contentsEqual(1).all())
            ones *= 2L
            Assert.assertTrue(ones.contentsEqual(2).all())
            ones *= IntType(3)
            Assert.assertTrue(ones.contentsEqual(6).all())
        }

        ArrayImgs.floats(1, 2, 3).plus(1.0F).let { ones ->
            Assert.assertTrue(ones.contentsEqual(1.0F).all())
            Assert.assertTrue((ones * FloatType(-1.0F)).contentsEqual(-1.0).all())
            Assert.assertTrue((ones * 3.0F).contentsEqual(3.0).all())
            Assert.assertTrue((ones * Math.PI).contentsEqual(Math.PI).all())
            Assert.assertTrue((FloatType(-1.0F) * ones).contentsEqual(-1.0).all())
            Assert.assertTrue((3F * ones).contentsEqual(3.0).all())
            Assert.assertTrue((Math.PI * ones).contentsEqual(Math.PI).all())

            ones *= 1.0
            Assert.assertTrue(ones.contentsEqual(1.0).all())
            ones *= 2.0F
            Assert.assertTrue(ones.contentsEqual(2.0).all())
            ones *= FloatType(3.0F)
            Assert.assertTrue(ones.contentsEqual(6.0).all())
        }
    }

    @Test
    fun divTest() {
        val rng = Random(100L)
        val img1 = ArrayImgs.ints(1, 2, 3) as RandomAccessibleInterval<IntType>
        val img2 = ArrayImgs.ints(1, 2, 3) as RandomAccessibleInterval<IntType>
        img1.iterable().forEach { it.set(rng.nextInt(1, 10)) }
        img2.iterable().forEach { it.set(rng.nextInt(1, 10)) }
        Assert.assertFalse(img1.contentsEqual(0).all())
        Assert.assertFalse(img2.contentsEqual(0).all())
        val diff1 = img1.copy()
        diff1 /= img2
        val diff2 = img1 / img2
        Assert.assertTrue(diff1.contentsEqual(diff2).all())
        Assert.assertTrue(diff1.contentsEqual(Views.interval(Converters.convert(Views.pair(img1, img2), { s, t -> t.set(s.a); t.divAssign(s.b)}, IntType()), img1)).all())

        ArrayImgs.ints(1, 2, 3).plus(10).let { tens ->
            Assert.assertTrue(tens.contentsEqual(10).all())
            Assert.assertTrue((tens / IntType(-1)).contentsEqual(-10).all())
            Assert.assertTrue((tens / 2).contentsEqual(5).all())
            Assert.assertTrue((tens / 3L).contentsEqual(3).all())
            Assert.assertTrue((IntType(-10) / tens).contentsEqual(-1).all())
            Assert.assertTrue((56 / tens).contentsEqual(5).all())
            Assert.assertTrue((30L / tens).contentsEqual(3).all())

            tens /= 2
            Assert.assertTrue(tens.contentsEqual(5).all())
            tens /= -3L
            Assert.assertTrue(tens.contentsEqual(-1).all())
            tens /= IntType(4)
            Assert.assertTrue(tens.contentsEqual(0).all())
        }

        ArrayImgs.floats(1, 2, 3).plus(1.0F).let { ones ->
            Assert.assertTrue(ones.contentsEqual(1.0F).all())
            Assert.assertTrue((ones / FloatType(-1.0F)).contentsEqual(-1.0).all())
            Assert.assertTrue((ones / 3.0F).contentsEqual(1.0 / 3.0).all())
            Assert.assertTrue((ones / 2.0).contentsEqual(0.5).all())
            Assert.assertTrue((FloatType(-1.0F) / ones).contentsEqual(-1.0).all())
            Assert.assertTrue((3.0F / ones).contentsEqual(3.0).all())
            Assert.assertTrue((2.0 / ones).contentsEqual(2.0).all())


            ones /= -2.0
            Assert.assertTrue(ones.contentsEqual(-0.5).all())
            ones /= 3.0F
            Assert.assertTrue(ones.contentsEqual(-0.5 / 3.0).all())
            ones /= FloatType(-4.0F)
            Assert.assertTrue(ones.contentsEqual(0.5 / 3.0 / 4.0).all())
        }
    }

    @Test
    fun unaryOpsTest() {
        val rng = Random(100L)
        val img = ArrayImgs.ints(1, 2, 3) as RandomAccessibleInterval<IntType>
        img.iterable().forEach { it.setInteger(rng.nextInt(1, 10).toLong()) }
        Assert.assertFalse(img.contentsEqual(0).any())
        Assert.assertTrue(Converters.convert(img, { s, t -> t.integer = +s.integer }, IntType()).contentsEqual(+img).all())
        Assert.assertTrue(Converters.convert(img, { s, t -> t.integer = -s.integer }, IntType()).contentsEqual(-img).all())
        val thresholded = Converters.convert(img, { s, t -> t.set(s.integer > 5) }, BitType())
        Assert.assertFalse(thresholded.contentsEqual(thresholded.not()).any())
    }

    @Test
    fun funcTest() {
        val rng = Random(100L)
        val X = DoubleArray(100, {rng.nextDouble()})
        val img = ArrayImgs.doubles(X, X.size.toLong())
        Assert.assertFalse(img.contentsEqual(0.0).all())
        Assert.assertArrayEquals(X.map { Math.exp(it) }.toDoubleArray(), img.exp().iterable().map { it.realDouble }.toDoubleArray(), 0.0)
        Assert.assertArrayEquals(X.map { Math.sqrt(it) }.toDoubleArray(), img.apply(DoubleUnaryOperator { Math.sqrt(it) }).iterable().map { it.realDouble }.toDoubleArray(), 0.0)
    }

}