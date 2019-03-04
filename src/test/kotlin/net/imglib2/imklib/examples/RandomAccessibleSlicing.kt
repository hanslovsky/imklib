package net.imglib2.imklib.examples

import net.imglib2.RandomAccessibleInterval
import net.imglib2.img.array.ArrayImgs
import net.imglib2.imklib.extensions.ALL
import net.imglib2.imklib.extensions.DOM
import net.imglib2.imklib.extensions.all
import net.imglib2.imklib.extensions.contentsEqual
import net.imglib2.imklib.extensions.get
import net.imglib2.imklib.extensions.iterable
import net.imglib2.imklib.extensions.maxAsLongs
import net.imglib2.imklib.extensions.minAsLongs
import net.imglib2.view.Views
import java.util.Arrays

fun main() {
    val rai = ArrayImgs.ints((0 until 60).map { it }.toIntArray(), 3, 4, 5)
    for (i in 0 until 3) {
        val sl1 = rai[i, ALL]
        Views.hyperSlice(rai, 0, i.toLong()).let {
            println(it.contentsEqual(sl1[it]).all())
            for (j in 0 until 4) {
                val sl2 = sl1[j, DOM]
                Views.hyperSlice(it, 0, j.toLong()).let {
                    println(it.contentsEqual(sl2[it]).all())
                }
            }
        }
    }

    for (i in 0 until 3) {
        Views.hyperSlice(rai, 0, i.toLong()).let { println(it.contentsEqual(rai[i, ALL][it]).all()) }
    }

    for (j in 0 until 4) {
        Views.hyperSlice(rai, 1, j.toLong()).let { println(it.contentsEqual(rai[DOM, j][it]).all()) }
    }

    for (k in 0 until 5) {
        Views.hyperSlice(rai, 2, k.toLong()).let { println(it.contentsEqual(rai[ALL, k][it]).all()) }
    }

    val rai1D = ArrayImgs.ints((0 until 5).map { it }.toIntArray(), 5)
    fun raiToString(img: RandomAccessibleInterval<*>) = img.iterable().joinToString(", ", prefix = "RAI[", postfix = "]") { it.toString() }
    println(raiToString(rai1D))
    println(raiToString(rai1D[-DOM]))
    println(raiToString(rai1D[1 .. 3]))
    println(raiToString(rai1D[1 .. 3][-DOM]))
    println(raiToString(rai1D[3 downTo  1]))
    println(raiToString(rai1D[3 downTo  1][-DOM]))
    println(rai1D[1 .. 3].minAsLongs().joinToString(", "))
    println(rai1D[1 .. 3].maxAsLongs().joinToString(", "))
    println(raiToString(rai1D[0 .. 4 step 3]))
    println(raiToString(rai1D[4 downTo 0 step 2]))
    println(Arrays.toString(rai1D[1 .. 4 step 2].minAsLongs()))
    println(Arrays.toString(rai1D[1 .. 4 step 2].maxAsLongs()))
    println(raiToString(rai1D[1 .. 4 step 2][DOM]))
    println(raiToString(rai1D[1 .. 4 step 2][-DOM]))

}