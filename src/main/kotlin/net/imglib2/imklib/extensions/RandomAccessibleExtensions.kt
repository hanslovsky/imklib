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

import net.imglib2.FinalInterval
import net.imglib2.Interval
import net.imglib2.Localizable
import net.imglib2.RandomAccessible
import net.imglib2.RandomAccessibleInterval
import net.imglib2.loops.LoopBuilder
import net.imglib2.type.Type
import net.imglib2.view.Views
import java.util.function.BiConsumer

operator fun <T> RandomAccessible<T>.get(vararg pos: Long): T = randomAccess().get(*pos)

operator fun <T> RandomAccessible<T>.get(vararg pos: Int): T = randomAccess().get(*pos)

operator fun <T> RandomAccessible<T>.get(pos: Localizable): T = randomAccess()[pos]

operator fun <T> RandomAccessible<T>.get(interval: Interval): RandomAccessibleInterval<T> = Views.interval(this, interval)
operator fun <T> RandomAccessible<T>.get(min: LongArray, max: LongArray) = get(FinalInterval(min, max))

operator fun <T> RandomAccessible<T>.get(interval: Interval, vararg pos: Long): T = randomAccess(interval).get(*pos)

operator fun <T> RandomAccessible<T>.get(interval: Interval, vararg pos: Int): T = randomAccess(interval).get(*pos)

operator fun <T> RandomAccessible<T>.get(interval: Interval, pos: Localizable): T = randomAccess(interval)[pos]

fun <T: Type<T>> RandomAccessible<T>.burnIn(target: RandomAccessibleInterval<T>): RandomAccessibleInterval<T> {
    LoopBuilder
            .setImages(Views.interval(this, target), target)
            .forEachPixel(BiConsumer { s, t -> t.set(s) })
    return target
}

fun <T: Type<T>> RandomAccessible<T>.burnIn(interval: Interval) = Views.interval(this, interval).copy()


// TODO do we need this for RandomAccessible as well? If so, implement in meaningful manner!
//operator fun <T> RandomAccessible<T>.get(vararg slicing: Any): RandomAccessible<T> {
//    slicing.forEach { require(isValidGetArg(it)) { "Not a valid slicing argument: $it" } }
//
//    if (slicing.any { it is _Axis && it.let { it.min != null || it.max != null } }) {
//        require(slicing.all { it is _Axis && it.let { it.min != null && it.max != null } }) { "If any bounded ax argument is passed, all arguments must be bounded axes!" }
//
//        val axes = slicing.map { it as _Axis }
//
//        val min = axes.map { it.min!! }.toLongArray()
//        val max = axes.map { it.max!! }.toLongArray()
//        val steps = axes.map { it.step }.toLongArray()
//        return this[min, max].let { if (steps.any { it > 1 }) Views.subsample(it, *steps) else it }
//    }
//
//    if (slicing.any { it is ALL }) {
//        require(slicing.filter { it is ALL }.size <= 1) { "Using more than one ALL object is ambiguous" }
//        val sliceIndex = slicing.indexOfFirst { it is ALL }.let { if (it == -1) slicing.size - 1 else it }
//        val indices = Array<Any>(numDimensions()) {SL}
//        slicing.asList().subList(0, sliceIndex).forEachIndexed { index, any -> indices[index] = any }
//        slicing.asList().subList(sliceIndex + 1, slicing.size).reversed().forEachIndexed { index, any -> indices[indices.size - 1 - index] = any}
//        return get(*indices)
//    }
//
//    else if (slicing.size < numDimensions()) {
//        val combined = listOf(*slicing) + listOf<Any>(ALL)
//        return get(*combined.toTypedArray())
//    }
//
//    var sliced = this
//    slicing.withIndex().forEach {
//        sliced = if (it.value is _Axis && (it.value as _Axis).step >= 0)
//            sliced else if (it.value is _Axis)
//            Views.invertAxis(sliced, it.index)
//            else
//            Views.hyperSlice(sliced, it.index, asLong(it.value))
//    }
//    return sliced
//}

fun isValidGetArg(arg: Any): Boolean = arg is ALL || arg is Slice || isInt(arg)

fun isInt(arg: Any) = arg is Long || arg is Int
fun asLong(arg: Any): Long {
    if (arg is Int) return arg.toLong()
    else if (arg is Long) return arg
    require(false, {"Int or Long required"})
    return 0L
}

// all axes
object ALL

