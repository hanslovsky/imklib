package net.imglib2.imklib.extensions

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