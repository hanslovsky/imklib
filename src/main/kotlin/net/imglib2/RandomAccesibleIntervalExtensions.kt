package net.imglib2

import net.imglib2.converter.Converters
import net.imglib2.loops.LoopBuilder
import net.imglib2.type.BooleanType
import net.imglib2.type.Type
import net.imglib2.type.logic.BitType
import net.imglib2.type.numeric.IntegerType
import net.imglib2.type.numeric.NumericType
import net.imglib2.type.numeric.RealType
import net.imglib2.type.operators.ValueEquals
import net.imglib2.util.Intervals
import net.imglib2.util.Util
import net.imglib2.view.Views
import java.util.function.BiConsumer
import java.util.function.Predicate

import net.imglib2.type.numeric.real.createVariable
import net.imglib2.type.numeric.integer.createVariable


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
