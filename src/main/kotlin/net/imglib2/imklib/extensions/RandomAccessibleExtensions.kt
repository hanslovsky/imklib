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

operator fun <T> RandomAccessible<T>.get(vararg slicing: Any): RandomAccessible<T> {
    slicing.forEach { require(isValidGetArg(it)) { "Not a valid slicing argument: $it" } }

    if (slicing.any { isProgression(it) }) {
        require(slicing.all { isProgression(it) }) { "If any range argument is passed, all arguments must be ranges!" }

        val min = slicing.map { if (it is IntProgression) it.first.toLong() else if (it is LongProgression) it.last else Long.MIN_VALUE }.toLongArray()
        val max = slicing.map { if (it is IntProgression) it.last.toLong() else if (it is LongProgression) it.last else Long.MAX_VALUE }.toLongArray()
        val steps = slicing.map { if (it is IntProgression) it.step.toLong() else if (it is LongProgression) it.step else Long.MAX_VALUE }.toLongArray()

        return (if (steps.any { it > 1 }) Views.subsample(this, *steps) else this)[min, max]
    }

    if (slicing.any { it is ALL }) {
        require(slicing.filter { it is ALL }.size <= 1) { "Using more than one ALL object is ambiguous" }
        val sliceIndex = slicing.indexOfFirst { it is ALL }.let { if (it == -1) slicing.size - 1 else it }
        val indices = Array<Any>(numDimensions(), {DOM})
        slicing.asList().subList(0, sliceIndex).forEachIndexed { index, any -> indices[index] = any }
        slicing.asList().subList(sliceIndex + 1, slicing.size).reversed().forEachIndexed { index, any -> indices[indices.size - 1 - index] = any}
        return get(*indices)
    }
    else if (slicing.size < numDimensions()) {
        val combined = listOf(*slicing) + listOf<Any>(ALL)
        return get(*combined.toTypedArray())
    }

    var sliced = this
    slicing.withIndex().forEach { sliced = if (it.value is DOM) sliced else Views.hyperSlice(sliced, it.index, asLong(it.value)) }
    return sliced
}

fun isValidGetArg(arg: Any): Boolean {
    return arg is ALL || arg is DOM || isInt(arg) || isProgression(arg)
}

fun isInt(arg: Any) = arg is Long || arg is Int
fun asLong(arg: Any): Long {
    if (arg is Int) return arg.toLong()
    else if (arg is Long) return arg
    require(false, {"Int or Long required"})
    return 0L
}

fun isProgression(arg: Any) = arg is IntProgression || arg is LongProgression

// all axes
object ALL

// entire domain for axis
object DOM