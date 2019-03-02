package net.imglib2.img

import net.imglib2.RandomAccessibleInterval
import net.imglib2.loops.LoopBuilder
import net.imglib2.plusAssign
import net.imglib2.type.numeric.NumericType
import java.util.function.BiConsumer

operator fun <T: NumericType<T>> Img<T>.plus(other: RandomAccessibleInterval<T>): Img<T> {
    val copy = this.factory().create(this)
    LoopBuilder.setImages(this, copy).forEachPixel(BiConsumer{t, c -> c.set(t)})
    copy += other
    return copy
}