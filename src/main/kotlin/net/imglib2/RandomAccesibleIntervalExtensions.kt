package net.imglib2

import net.imglib2.loops.LoopBuilder
import net.imglib2.type.numeric.NumericType
import java.util.function.BiConsumer

operator fun <T: NumericType<T>> RandomAccessibleInterval<T>.plusAssign(other: RandomAccessibleInterval<T>) {
    LoopBuilder.setImages(this, other).forEachPixel(BiConsumer{ t, o -> t.add(o)})
}