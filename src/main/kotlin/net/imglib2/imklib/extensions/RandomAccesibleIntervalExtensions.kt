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

import net.imglib2.*
import net.imglib2.converter.Converter
import net.imglib2.converter.Converters
import net.imglib2.loops.LoopBuilder
import net.imglib2.type.BooleanType
import net.imglib2.type.Type
import net.imglib2.type.logic.BitType
import net.imglib2.type.numeric.IntegerType
import net.imglib2.type.numeric.NumericType
import net.imglib2.type.numeric.RealType
import net.imglib2.type.numeric.integer.IntType
import net.imglib2.type.numeric.real.DoubleType
import net.imglib2.type.operators.ValueEquals
import net.imglib2.util.ConstantUtils
import net.imglib2.util.Intervals
import net.imglib2.util.Pair
import net.imglib2.util.Util
import net.imglib2.view.Views
import java.util.function.BiConsumer
import java.util.function.DoubleUnaryOperator
import java.util.function.Predicate


// plus
operator fun <T: NumericType<T>> RandomAccessibleInterval<T>.plusAssign(other: RandomAccessibleInterval<T>) {
    require(numDimensions() == other.numDimensions(), {"Num dimensions mismatch: $this $other"})
    (0 until numDimensions()).forEach { require(dimension(it) == other.dimension(it), {"Dimension $it mismatch: $this $other"}) }
    LoopBuilder.setImages(this, other).forEachPixel(BiConsumer{ t, o -> t.add(o)})
}

operator fun <T: NumericType<T>> RandomAccessibleInterval<T>.plus(other: RandomAccessibleInterval<T>): RandomAccessibleInterval<T> {
    val copy = copy()
    copy += other
    return if (isZeroMin()) copy else Views.translate(copy, *this.minAsLongs())
}

// minus
operator fun <T: NumericType<T>> RandomAccessibleInterval<T>.minusAssign(other: RandomAccessibleInterval<T>) {
    require(numDimensions() == other.numDimensions(), {"Num dimensions mismatch: $this $other"})
    (0 until numDimensions()).forEach { require(dimension(it) == other.dimension(it), {"Dimension $it mismatch: $this $other"}) }
    LoopBuilder.setImages(this, other).forEachPixel(BiConsumer{ t, o -> t.sub(o)})
}

operator fun <T: NumericType<T>> RandomAccessibleInterval<T>.minus(other: RandomAccessibleInterval<T>): RandomAccessibleInterval<T> {
    val copy = copy()
    copy -= other
    return if (isZeroMin()) copy else Views.translate(copy, *this.minAsLongs())
}

// times
operator fun <T: NumericType<T>> RandomAccessibleInterval<T>.timesAssign(other: RandomAccessibleInterval<T>) {
    require(numDimensions() == other.numDimensions(), {"Num dimensions mismatch: $this $other"})
    (0 until numDimensions()).forEach { require(dimension(it) == other.dimension(it), {"Dimension $it mismatch: $this $other"}) }
    LoopBuilder.setImages(this, other).forEachPixel(BiConsumer{ t, o -> t.mul(o)})
}

operator fun <T: NumericType<T>> RandomAccessibleInterval<T>.times(other: RandomAccessibleInterval<T>): RandomAccessibleInterval<T> {
    val copy = copy()
    copy *= other
    return if (isZeroMin()) copy else Views.translate(copy, *this.minAsLongs())
}

// minus
operator fun <T: NumericType<T>> RandomAccessibleInterval<T>.divAssign(other: RandomAccessibleInterval<T>) {
    require(numDimensions() == other.numDimensions(), {"Num dimensions mismatch: $this $other"})
    (0 until numDimensions()).forEach { require(dimension(it) == other.dimension(it), {"Dimension $it mismatch: $this $other"}) }
    LoopBuilder.setImages(this, other).forEachPixel(BiConsumer{ t, o -> t.div(o)})
}

operator fun <T: NumericType<T>> RandomAccessibleInterval<T>.div(other: RandomAccessibleInterval<T>): RandomAccessibleInterval<T> {
    val copy = copy()
    copy /= other
    return if (isZeroMin()) copy else Views.translate(copy, *this.minAsLongs())
}

// plus
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.plusAssign(value: T) = iterable().forEach { it += value }
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.plusAssign(value: Int) = plusAssign(createType(value))
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.plusAssign(value: Long) = plusAssign(createType(value))
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.plus(value: T) = copy().let {it.iterable().forEach { it += value }; it}
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.plus(value: Int) = plus(createType(value))
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.plus(value: Long) = plus(createType(value))

operator fun <T: RealType<T>> RandomAccessibleInterval<T>.plusAssign(value: T) = iterable().forEach { it += value }
operator fun <T: RealType<T>> RandomAccessibleInterval<T>.plusAssign(value: Float) = plusAssign(createType(value))
operator fun <T: RealType<T>> RandomAccessibleInterval<T>.plusAssign(value: Double) = plusAssign(createType(value))
operator fun <T: RealType<T>> RandomAccessibleInterval<T>.plus(value: T) = copy().let{ it.iterable().forEach { it += value }; it }
operator fun <T: RealType<T>> RandomAccessibleInterval<T>.plus(value: Float) = plus(createType(value))
operator fun <T: RealType<T>> RandomAccessibleInterval<T>.plus(value: Double) = plus(createType(value))

operator fun <T: IntegerType<T>> T.plus(rai: RandomAccessibleInterval<T>) = rai.copy().let {it.iterable().forEach { it += this }; it}
operator fun <T: IntegerType<T>> Int.plus(rai: RandomAccessibleInterval<T>) = rai.createType(this) + rai
operator fun <T: IntegerType<T>> Long.plus(rai: RandomAccessibleInterval<T>) = rai.createType(this) + rai

operator fun <T: RealType<T>> T.plus(rai: RandomAccessibleInterval<T>) = rai.copy().let {it.iterable().forEach { it += this }; it}
operator fun <T: RealType<T>> Float.plus(rai: RandomAccessibleInterval<T>) = rai.createType(this) + rai
operator fun <T: RealType<T>> Double.plus(rai: RandomAccessibleInterval<T>) = rai.createType(this) + rai

// minus
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.minusAssign(value: T) = iterable().forEach { it -= value }
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.minusAssign(value: Int) = minusAssign(createType(value))
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.minusAssign(value: Long) = minusAssign(createType(value))
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.minus(value: T) = copy().let {it.iterable().forEach { it -= value }; it}
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.minus(value: Int) = minus(createType(value))
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.minus(value: Long) = minus(createType(value))

operator fun <T: RealType<T>> RandomAccessibleInterval<T>.minusAssign(value: T) = iterable().forEach { it -= value }
operator fun <T: RealType<T>> RandomAccessibleInterval<T>.minusAssign(value: Float) = minusAssign(createType(value))
operator fun <T: RealType<T>> RandomAccessibleInterval<T>.minusAssign(value: Double) = minusAssign(createType(value))
operator fun <T: RealType<T>> RandomAccessibleInterval<T>.minus(value: T) = copy().let{ it.iterable().forEach { it -= value }; it }
operator fun <T: RealType<T>> RandomAccessibleInterval<T>.minus(value: Float) = minus(createType(value))
operator fun <T: RealType<T>> RandomAccessibleInterval<T>.minus(value: Double) = minus(createType(value))

operator fun <T: IntegerType<T>> T.minus(rai: RandomAccessibleInterval<T>) = ConstantUtils.constantRandomAccessibleInterval(this, rai.numDimensions(), rai) - rai
operator fun <T: IntegerType<T>> Int.minus(rai: RandomAccessibleInterval<T>) = rai.createType(this) - rai
operator fun <T: IntegerType<T>> Long.minus(rai: RandomAccessibleInterval<T>) = rai.createType(this) - rai

operator fun <T: RealType<T>> T.minus(rai: RandomAccessibleInterval<T>) = ConstantUtils.constantRandomAccessibleInterval(this, rai.numDimensions(), rai) - rai
operator fun <T: RealType<T>> Float.minus(rai: RandomAccessibleInterval<T>) = rai.createType(this) - rai
operator fun <T: RealType<T>> Double.minus(rai: RandomAccessibleInterval<T>) = rai.createType(this) - rai

// times
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.timesAssign(value: T) = iterable().forEach { it *= value }
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.timesAssign(value: Int) = timesAssign(createType(value))
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.timesAssign(value: Long) = timesAssign(createType(value))
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.times(value: T) = copy().let {it.iterable().forEach { it *= value }; it}
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.times(value: Int) = times(createType(value))
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.times(value: Long) = times(createType(value))

operator fun <T: RealType<T>> RandomAccessibleInterval<T>.timesAssign(value: T) = iterable().forEach { it *= value }
operator fun <T: RealType<T>> RandomAccessibleInterval<T>.timesAssign(value: Float) = timesAssign(createType(value))
operator fun <T: RealType<T>> RandomAccessibleInterval<T>.timesAssign(value: Double) = timesAssign(createType(value))
operator fun <T: RealType<T>> RandomAccessibleInterval<T>.times(value: T) = copy().let{ it.iterable().forEach { it *= value }; it }
operator fun <T: RealType<T>> RandomAccessibleInterval<T>.times(value: Float) = times(createType(value))
operator fun <T: RealType<T>> RandomAccessibleInterval<T>.times(value: Double) = times(createType(value))

operator fun <T: IntegerType<T>> T.times(rai: RandomAccessibleInterval<T>) = rai.copy().let {it.iterable().forEach { it *= this }; it}
operator fun <T: IntegerType<T>> Int.times(rai: RandomAccessibleInterval<T>) = rai.createType(this) * rai
operator fun <T: IntegerType<T>> Long.times(rai: RandomAccessibleInterval<T>) = rai.createType(this) * rai

operator fun <T: RealType<T>> T.times(rai: RandomAccessibleInterval<T>) = rai.copy().let {it.iterable().forEach { it *= this }; it}
operator fun <T: RealType<T>> Float.times(rai: RandomAccessibleInterval<T>) = rai.createType(this) * rai
operator fun <T: RealType<T>> Double.times(rai: RandomAccessibleInterval<T>) = rai.createType(this) * rai

// div
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.divAssign(value: T) = iterable().forEach { it /= value }
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.divAssign(value: Int) = divAssign(createType(value))
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.divAssign(value: Long) = divAssign(createType(value))
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.div(value: T) = copy().let {it.iterable().forEach { it /= value }; it}
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.div(value: Int) = div(createType(value))
operator fun <T: IntegerType<T>> RandomAccessibleInterval<T>.div(value: Long) = div(createType(value))

operator fun <T: RealType<T>> RandomAccessibleInterval<T>.divAssign(value: T) = iterable().forEach { it /= value }
operator fun <T: RealType<T>> RandomAccessibleInterval<T>.divAssign(value: Float) = divAssign(createType(value))
operator fun <T: RealType<T>> RandomAccessibleInterval<T>.divAssign(value: Double) = divAssign(createType(value))
operator fun <T: RealType<T>> RandomAccessibleInterval<T>.div(value: T) = copy().let{ it.iterable().forEach { it /= value }; it }
operator fun <T: RealType<T>> RandomAccessibleInterval<T>.div(value: Float) = div(createType(value))
operator fun <T: RealType<T>> RandomAccessibleInterval<T>.div(value: Double) = div(createType(value))

operator fun <T: IntegerType<T>> T.div(rai: RandomAccessibleInterval<T>) = ConstantUtils.constantRandomAccessibleInterval(this, rai.numDimensions(), rai) / rai
operator fun <T: IntegerType<T>> Int.div(rai: RandomAccessibleInterval<T>) = rai.createType(this) / rai
operator fun <T: IntegerType<T>> Long.div(rai: RandomAccessibleInterval<T>) = rai.createType(this) / rai

operator fun <T: RealType<T>> T.div(rai: RandomAccessibleInterval<T>) = ConstantUtils.constantRandomAccessibleInterval(this, rai.numDimensions(), rai) / rai
operator fun <T: RealType<T>> Float.div(rai: RandomAccessibleInterval<T>) = rai.createType(this) / rai
operator fun <T: RealType<T>> Double.div(rai: RandomAccessibleInterval<T>) = rai.createType(this) / rai

fun <T> RandomAccessibleInterval<T>.minAsLongs() = Intervals.minAsLongArray(this)
fun <T> RandomAccessibleInterval<T>.maxAsLongs() = Intervals.maxAsLongArray(this)
fun <T> RandomAccessibleInterval<T>.minAsInts() = Intervals.minAsIntArray(this)
fun <T> RandomAccessibleInterval<T>.maxAsInts() = Intervals.maxAsIntArray(this)
fun <T> RandomAccessibleInterval<T>.minAsPoint() = Point(*minAsLongs())
fun <T> RandomAccessibleInterval<T>.maxAsPoint() = Point(*maxAsLongs())
fun <T> RandomAccessibleInterval<T>.isZeroMin() = Views.isZeroMin(this)
fun <T> RandomAccessibleInterval<T>.zeroMin() = Views.zeroMin(this)


fun <T> RandomAccessibleInterval<T>.getType() = get(*maxAsLongs())

fun <T: Type<T>> RandomAccessibleInterval<T>.createType() = getType().createVariable()
fun <T: RealType<T>> RandomAccessibleInterval<T>.createType(value: Float) = getType().createVariable(value)
fun <T: RealType<T>> RandomAccessibleInterval<T>.createType(value: Double) = getType().createVariable(value)
fun <T: IntegerType<T>> RandomAccessibleInterval<T>.createType(value: Int) = getType().createVariable(value)
fun <T: IntegerType<T>> RandomAccessibleInterval<T>.createType(value: Long) = getType().createVariable(value)

fun <T: Type<T>> RandomAccessibleInterval<T>.factory() = Util.getSuitableImgFactory(this, createType())
fun <T: Type<T>> RandomAccessibleInterval<T>.copy(): RandomAccessibleInterval<T> {
    val copy = factory().create(this).let{ if (isZeroMin()) it else Views.translate(it, *this.minAsLongs()) as RandomAccessibleInterval<T> }
    burnIn(copy)
    return copy
}

fun <T> RandomAccessibleInterval<T>.testElements(test: Predicate<T>) = convertTo(BitType()) { src, tgt -> tgt.set(test.test(src)) }
fun <T> RandomAccessibleInterval<T>.testElements(test: (T) -> Boolean) = testElements(Predicate { test(it) })
fun <T: ValueEquals<T>> RandomAccessibleInterval<T>.elementsEqual(ref: T) = testElements { ref.valueEquals(it) }
fun <T: RealType<T>> RandomAccessibleInterval<T>.elementsEqual(ref: Float) = elementsEqual(createType(ref))
fun <T: RealType<T>> RandomAccessibleInterval<T>.elementsEqual(ref: Double) = elementsEqual(createType(ref))
fun <T: IntegerType<T>> RandomAccessibleInterval<T>.elementsEqual(ref: Int) = elementsEqual(createType(ref))
fun <T: IntegerType<T>> RandomAccessibleInterval<T>.elementsEqual(ref: Long) = elementsEqual(createType(ref))

fun <T: Comparable<T>> RandomAccessibleInterval<T>.compareElementsTo(ref: T) = convertTo(IntType()) { src, tgt -> tgt.set(ref.compareTo(src)) }
fun <T: Comparable<T>> RandomAccessibleInterval<T>.elmentsLargerThan(comparison: T) = compareElementsTo(comparison).elementsEqual(1)
fun <T: Comparable<T>> RandomAccessibleInterval<T>.elementsSmallerThan(comparison: T) = compareElementsTo(comparison).elementsEqual(-1)
fun <T: Comparable<T>> RandomAccessibleInterval<T>.elmentsLargerThanOrEqual(comparison: T) = compareElementsTo(comparison).testElements { it.get() >= 0 }
fun <T: Comparable<T>> RandomAccessibleInterval<T>.elementsSmallerThanOrEqual(comparison: T) = compareElementsTo(comparison).testElements { it.get() <= 0 }


fun <T, U: Type<U>> RandomAccessibleInterval<T>.convertTo(u: U, converter: Converter<T, U>) = Converters.convert(this, converter, u.createVariable())
fun <T, U: Type<U>> RandomAccessibleInterval<T>.convertTo(u: U, converter: (T, U) -> Unit) = convertTo(u, Converter { src, tgt -> converter(src, tgt) })

fun <T: Type<T>> RandomAccessibleInterval<T>.unique() = HashSet<T>().let {iterable().forEach { t -> if (!it.contains(t)) it.add(t.copy()) }; it }

fun <T: ValueEquals<T>> RandomAccessibleInterval<T>.elementsEqual(that: RandomAccessibleInterval<T>): RandomAccessibleInterval<BitType> {
    require(Intervals.equalDimensions(this, that), {"Dimensionality mismatch: $this $that"})
    val paired = Views.translate(Views.interval(Views.pair(Views.zeroMin(this), Views.zeroMin(that)), Views.zeroMin(this)), *this.minAsLongs())
    return Converters.convert(paired as RandomAccessibleInterval<Pair<T, T>>, { s, t -> t.set(s.a.valueEquals(s.b)) }, BitType())
}

fun <T> RandomAccessibleInterval<T>.all(predicate: (T) -> Boolean): Boolean = iterable().all(predicate)
fun <T> RandomAccessibleInterval<T>.any(predicate: (T) -> Boolean): Boolean = iterable().any(predicate)
fun <T> RandomAccessibleInterval<T>.all(predicate: Predicate<T>): Boolean = all(predicate::test)
fun <T> RandomAccessibleInterval<T>.any(predicate: Predicate<T>): Boolean = any(predicate::test)
fun <B: BooleanType<B>> RandomAccessibleInterval<B>.all() = all {it.get()}
fun <B: BooleanType<B>> RandomAccessibleInterval<B>.any() = any {it.get()}

fun <T> RandomAccessibleInterval<T>.iterable() = Views.iterable(this)
fun <T> RandomAccessibleInterval<T>.flatIterable() = Views.flatIterable(this)

// TODO should these create copies or views?
operator fun <B: BooleanType<B>> RandomAccessibleInterval<B>.not() = Converters.convert(this, { s, t -> t.set(!s.get())}, createType())
operator fun <T: Type<T>> RandomAccessibleInterval<T>.unaryPlus()= Converters.convert(this, { s, t -> t.set(s) }, createType())
operator fun <T: NumericType<T>> RandomAccessibleInterval<T>.unaryMinus() = Converters.convert(this, { s, t -> t.set(s); t.mul(-1.0) }, createType())

// TODO should these create copies or views?
fun <T: RealType<T>, U: RealType<U>> RandomAccessibleInterval<T>.apply(func: DoubleUnaryOperator, dtype: U) = Converters.convert(this, { s, t -> t.setReal(func.applyAsDouble(s.realDouble))}, dtype).copy()
fun <T: RealType<T>> RandomAccessibleInterval<T>.apply(func: DoubleUnaryOperator) = apply(func, DoubleType())

fun <T: RealType<T>, U: RealType<U>> RandomAccessibleInterval<T>.apply(func: (Double) -> Double, dtype: U) = apply(DoubleUnaryOperator { func(it) }, dtype)
fun <T: RealType<T>> RandomAccessibleInterval<T>.apply(func: (Double) -> Double) = apply(func, DoubleType())

fun <T: RealType<T>, U: RealType<U>> RandomAccessibleInterval<T>.exp(dtype: U) = apply(DoubleUnaryOperator{ Math.exp(it) }, dtype)
fun <T: RealType<T>> RandomAccessibleInterval<T>.exp() = exp(DoubleType())

operator fun <T> RandomAccessibleInterval<T>.get(vararg pos: Long): T = randomAccess().get(*pos)

operator fun <T> RandomAccessibleInterval<T>.get(vararg pos: Int): T = randomAccess().get(*pos)

operator fun <T> RandomAccessibleInterval<T>.get(pos: Localizable): T = randomAccess()[pos]

operator fun <T> RandomAccessibleInterval<T>.get(interval: Interval): RandomAccessibleInterval<T> = Views.interval(this, interval)
operator fun <T> RandomAccessibleInterval<T>.get(min: LongArray, max: LongArray) = get(FinalInterval(min, max))

operator fun <T> RandomAccessibleInterval<T>.get(interval: Interval, vararg pos: Long): T = randomAccess(interval).get(*pos)

operator fun <T> RandomAccessibleInterval<T>.get(interval: Interval, vararg pos: Int): T = randomAccess(interval).get(*pos)

operator fun <T> RandomAccessibleInterval<T>.get(interval: Interval, pos: Localizable): T = randomAccess(interval)[pos]
operator fun <T> RandomAccessibleInterval<T>.get(vararg slicing: Any): RandomAccessibleInterval<T> {
    slicing.forEach { require(isValidGetArg(it)) { "Not a valid slicing argument: $it" } }
    if (slicing.any { it is ALL }) {
        require(slicing.filter { it is ALL }.size <= 1) { "Using more than one ALL object is ambiguous" }
        val sliceIndex = slicing.indexOfFirst { it is ALL }.let { if (it == -1) slicing.size - 1 else it }
        val indices = Array<Any>(numDimensions(), {SL})
        slicing.asList().subList(0, sliceIndex).forEachIndexed { index, any -> indices[index] = any }
        slicing.asList().subList(sliceIndex + 1, slicing.size).reversed().forEachIndexed { index, any -> indices[indices.size - 1 - index] = any}
        return get(*indices)
    }
    else if (slicing.size < numDimensions()) {
        val combined = listOf(*slicing) + listOf<Any>(ALL)
        return get(*combined.toTypedArray())
    }

    var sliced = this
    slicing.withIndex().reversed().forEach { sliced = if (it.value is Slice) sliced else Views.hyperSlice(sliced, it.index, asLong(it.value)) }

    return slicing.filter { it is Slice }.map { it as Slice }.let { axes ->
        if (axes.size == 0)
            sliced
        else {
            val min = axes.mapIndexed { index, ax -> if (ax.step < 0) ax.max?: max(index) else ax.min?: min(index) }.toLongArray()
            val max = axes.mapIndexed { index, ax -> if (ax.step < 0) ax.min?: min(index) else ax.max?: max(index) }.toLongArray()
            val steps = axes.map { it.step }.toLongArray()

            for (index in min.indices) {
                val m = min[index]
                val M = max[index]
                val s = steps[index]
                require((M - m) * s >= 0) {"Inconsistent range provided!"}
                if (M < m) {
                    val inverted = Views.invertAxis(sliced, index)
//                    println("${Arrays.toString(inverted.minAsLongs())} ${Arrays.toString(inverted.maxAsLongs())}")
                    sliced = Views.translate(inverted, m + M)
//                    println("${Arrays.toString(sliced.minAsLongs())} ${Arrays.toString(sliced.maxAsLongs())}")
                    min[index] = M
                    max[index] = m
                    steps[index] = -s
                }
            }

//            println("${Arrays.toString(sliced.minAsLongs())} ${Arrays.toString(sliced.maxAsLongs())} ${Arrays.toString(min)} ${Arrays.toString(max)} ${Arrays.toString(steps)}")
            sliced[min, max].let {if (steps.any { it > 1 }) Views.subsample(it, *steps) else it}
        }
    }
}
