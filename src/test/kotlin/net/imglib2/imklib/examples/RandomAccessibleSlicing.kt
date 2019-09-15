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
package net.imglib2.imklib.examples

import net.imglib2.RandomAccessibleInterval
import net.imglib2.img.array.ArrayImgs
import net.imglib2.imklib.extensions.ALL
import net.imglib2.imklib.extensions.SL
import net.imglib2.imklib.extensions.IN
import net.imglib2.imklib.extensions.EX
import net.imglib2.imklib.extensions.all
import net.imglib2.imklib.extensions.elementsEqual
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
            println(it.elementsEqual(sl1[it]).all())
            for (j in 0 until 4) {
                val sl2 = sl1[j, SL]
                Views.hyperSlice(it, 0, j.toLong()).let {
                    println(it.elementsEqual(sl2[it]).all())
                }
            }
        }
    }

    for (i in 0 until 3) {
        Views.hyperSlice(rai, 0, i.toLong()).let { println(it.elementsEqual(rai[i, ALL][it]).all()) }
    }

    for (j in 0 until 4) {
        Views.hyperSlice(rai, 1, j.toLong()).let { println(it.elementsEqual(rai[SL, j][it]).all()) }
    }

    for (k in 0 until 5) {
        Views.hyperSlice(rai, 2, k.toLong()).let { println(it.elementsEqual(rai[ALL, k][it]).all()) }
    }

    val rai1D = ArrayImgs.ints((0 until 5).map { it }.toIntArray(), 5)
    fun raiToString(img: RandomAccessibleInterval<*>) = img.iterable().joinToString(", ", prefix = "RAI[", postfix = "]") { it.toString() }
    println(raiToString(rai1D))
    println(raiToString(rai1D[-SL]))
    println(raiToString(rai1D[IN(1, 3)]))
    println(raiToString(rai1D[IN(1, 3)][-SL]))
    println(raiToString(rai1D[IN(1, 3, -1)]))
    println(rai1D[IN(1, 3)].minAsLongs().joinToString(", "))
    println(rai1D[IN(1, 3)].maxAsLongs().joinToString(", "))
    println(raiToString(rai1D[IN(0, 4, 2)]))
    println(raiToString(rai1D[EX(0, 5, 2)]))
    println(raiToString(rai1D[-SL[0, 4, 2]]))
    println(raiToString(rai1D[IN(step=-2)]))
    println(Arrays.toString(rai1D[IN(1, 4, 2)].minAsLongs()))
    println(Arrays.toString(rai1D[IN(1, 4, 2)].maxAsLongs()))
    println(raiToString(rai1D[IN(1, 4, 2)][+SL]))
    println(raiToString(rai1D[IN(1, 4, 2)][-SL]))
    println(raiToString(rai1D[SL..2]))
    println(raiToString(rai1D[SL ST 2]))
    println(raiToString(rai1D[1 EX 5L ST 1]))
    println(raiToString(rai1D[1 IN 4L ST 1]))
}
