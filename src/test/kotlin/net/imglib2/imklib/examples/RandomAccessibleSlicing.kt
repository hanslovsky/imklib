package net.imglib2.imklib.examples

import net.imglib2.img.array.ArrayImgs
import net.imglib2.imklib.extensions.ALL
import net.imglib2.imklib.extensions.DOM
import net.imglib2.imklib.extensions.all
import net.imglib2.imklib.extensions.contentsEqual
import net.imglib2.imklib.extensions.get
import net.imglib2.view.Views

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

}