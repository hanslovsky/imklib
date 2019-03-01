package net.imglib2

import net.imglib2.img.Img
import net.imglib2.loops.LoopBuilder
import net.imglib2.type.numeric.NumericType
import java.util.function.BiConsumer

operator fun <T: NumericType<T>> Img<T>.plus(other: RandomAccessibleInterval<T>): Img<T> {
    val copy = this.factory().create(this)
    LoopBuilder.setImages(this, copy).forEachPixel(BiConsumer{t, c -> c.set(t)})
    copy += other
    return copy
}