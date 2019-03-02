package net.imglib2

import net.imglib2.converter.Converters
import net.imglib2.loops.LoopBuilder
import net.imglib2.type.BooleanType
import net.imglib2.type.Type
import net.imglib2.type.logic.BitType
import net.imglib2.type.numeric.IntegerType
import net.imglib2.type.numeric.NumericType
import net.imglib2.type.numeric.RealType
import net.imglib2.type.numeric.integer.createVariable
import net.imglib2.type.numeric.integer.divAssign
import net.imglib2.type.numeric.integer.minusAssign
import net.imglib2.type.numeric.integer.plusAssign
import net.imglib2.type.numeric.integer.timesAssign
import net.imglib2.type.numeric.real.createVariable
import net.imglib2.type.numeric.real.divAssign
import net.imglib2.type.numeric.real.minusAssign
import net.imglib2.type.numeric.real.plusAssign
import net.imglib2.type.numeric.real.timesAssign
import net.imglib2.type.operators.ValueEquals
import net.imglib2.util.ConstantUtils
import net.imglib2.util.Intervals
import net.imglib2.util.Util
import net.imglib2.view.Views
import java.util.function.BiConsumer
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

fun <T: ValueEquals<T>> RandomAccessibleInterval<T>.contentsEqual(ref: T) = Converters.convert(this, { src, tgt -> tgt.set(src.valueEquals(ref)) }, BitType())
fun <T: RealType<T>> RandomAccessibleInterval<T>.contentsEqual(ref: Float) = contentsEqual(createType(ref))
fun <T: RealType<T>> RandomAccessibleInterval<T>.contentsEqual(ref: Double) = contentsEqual(createType(ref))
fun <T: IntegerType<T>> RandomAccessibleInterval<T>.contentsEqual(ref: Int) = contentsEqual(createType(ref))
fun <T: IntegerType<T>> RandomAccessibleInterval<T>.contentsEqual(ref: Long) = contentsEqual(createType(ref))

fun <T: ValueEquals<T>> RandomAccessibleInterval<T>.contentsEqual(that: RandomAccessibleInterval<T>): RandomAccessibleInterval<BitType> {
    require(Intervals.equalDimensions(this, that), {"Dimensionality mismatch: $this $that"})
    val paired = Views.translate(Views.interval(Views.pair(Views.zeroMin(this), Views.zeroMin(that)), Views.zeroMin(this)), *this.minAsLongs())
    return Converters.convert(paired as RandomAccessibleInterval<net.imglib2.util.Pair<T, T>>, { s, t -> t.set(s.a.valueEquals(s.b)) }, BitType())
}

fun <T> RandomAccessibleInterval<T>.all(predicate: (T) -> Boolean): Boolean = Views.iterable(this).all(predicate)
fun <T> RandomAccessibleInterval<T>.any(predicate: (T) -> Boolean): Boolean = Views.iterable(this).any(predicate)
fun <T> RandomAccessibleInterval<T>.all(predicate: Predicate<T>): Boolean = all(predicate::test)
fun <T> RandomAccessibleInterval<T>.any(predicate: Predicate<T>): Boolean = any(predicate::test)
fun <B: BooleanType<B>> RandomAccessibleInterval<B>.all() = all {it.get()}
fun <B: BooleanType<B>> RandomAccessibleInterval<B>.any() = any {it.get()}

fun <T> RandomAccessibleInterval<T>.iterable() = Views.iterable(this)
fun <T> RandomAccessibleInterval<T>.flatIterable() = Views.flatIterable(this)
