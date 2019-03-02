package net.imglib2

import net.imglib2.converter.Converters
import net.imglib2.img.array.ArrayImgs
import net.imglib2.type.numeric.integer.IntType
import net.imglib2.type.numeric.real.DoubleType
import net.imglib2.util.Intervals
import net.imglib2.view.Views
import org.junit.Assert
import org.junit.Test
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


        img.get(1, 2, 3).set(1)
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
    }

}