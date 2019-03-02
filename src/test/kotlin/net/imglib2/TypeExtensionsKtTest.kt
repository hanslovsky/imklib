package net.imglib2

import net.imglib2.type.numeric.integer.LongType
import net.imglib2.type.numeric.integer.plus
import net.imglib2.type.numeric.integer.plusAssign
import net.imglib2.type.numeric.real.DoubleType
import net.imglib2.type.numeric.real.plus
import net.imglib2.type.numeric.real.plusAssign
import org.junit.Assert
import org.junit.Test

class TypeExtensionsKtTest {

    @Test
    fun testAddReal() {
        val rt = DoubleType(1.0)
        Assert.assertEquals(DoubleType(2.0), rt + rt)
        Assert.assertEquals(DoubleType(2.0), rt + 1.0)
        Assert.assertEquals(DoubleType(3.0), rt + 2.0f)

        rt += rt
        Assert.assertEquals(DoubleType(2.0), rt)

        rt += 2.0
        Assert.assertEquals(DoubleType(4.0), rt)

        rt += 3.0F
        Assert.assertEquals(DoubleType(7.0), rt)
    }

    @Test
    fun testAddInteger() {
        val rt = LongType(1L)
        Assert.assertEquals(LongType(2L), rt + rt)
        Assert.assertEquals(LongType(2L), rt + 1)
        Assert.assertEquals(LongType(3L), rt + 2L)

        rt += rt
        Assert.assertEquals(LongType(2L), rt)

        rt += 2
        Assert.assertEquals(LongType(4L), rt)

        rt += 3L
        Assert.assertEquals(LongType(7L), rt)
    }

}