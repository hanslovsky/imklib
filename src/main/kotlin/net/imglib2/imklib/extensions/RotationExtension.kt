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

import net.imglib2.FinalRealInterval
import net.imglib2.RandomAccessible
import net.imglib2.RandomAccessibleInterval
import net.imglib2.interpolation.InterpolatorFactory
import net.imglib2.interpolation.randomaccess.NLinearInterpolatorFactory
import net.imglib2.realtransform.AffineTransform3D
import net.imglib2.realtransform.RealViews
import net.imglib2.type.numeric.NumericType
import net.imglib2.util.Intervals
import net.imglib2.view.Views

fun <T: NumericType<T>> RandomAccessibleInterval<T>.rotate(
        angle: Double,
        extension: (RandomAccessibleInterval<T>) -> RandomAccessible<T> = {Views.extendZero(it)},
        interpolation: InterpolatorFactory<T, RandomAccessible<T>> = NLinearInterpolatorFactory(),
        axis: Int = 2): RandomAccessibleInterval<T> {
    if (this.numDimensions() == 2)
        return Views.hyperSlice(Views.addDimension(this, 0L, 0L).rotate(angle, interpolation = interpolation, axis = 2), 2, 0L)
    require(this.numDimensions() == 3)
    require(axis in 0..2)

    // x
    // 1 0        0
    // 0 cos phi -sin phi
    // 0 sin phi  cos phi

    // y
    //  cos phi 0 sin phi
    //  0       1 0
    // -sin phi 0 cos phi

    // z
    // cos theta -sin theta 0
    // sin theta  cos theta 0
    // 0          0         1

    val cos = Math.cos(-angle * Math.PI / 180.0)
    val sin = Math.sin(-angle * Math.PI / 180.0)

    val rotation = when(axis) {
        0 -> AffineTransform3D().let {it.set(1.0, 0.0, 0.0, 0.0, 0.0, cos, -sin, 0.0, 0.0, sin, cos, 0.0); it }
        1 -> AffineTransform3D().let {it.set(cos, 0.0, sin, 0.0, 0.0, 1.0, 0.0, 0.0, -sin, 0.0, cos, 0.0); it }
        2 -> AffineTransform3D().let {it.set(cos, -sin, 0.0, 0.0, sin, cos, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0); it }
        else -> throw RuntimeException("Only three-dimensional data supported")
    }

    val translation = AffineTransform3D().let {it.setTranslation(*((0..2).map { if (it == axis) 0.0 else 0.5 * (min(it) + max(it)) }.toDoubleArray())); it}
    // TODO update bounding box, or just return (Real)RandomAccessible
    val transform = translation.copy().concatenate(rotation).concatenate(translation.inverse())


    val transformed = RealViews.affine(Views.interpolate(extension(this), interpolation), transform)
    val points = arrayOf(
            doubleArrayOf(min(0).toDouble(), min(1).toDouble(), min(2).toDouble()),
            doubleArrayOf(min(0).toDouble(), min(1).toDouble(), max(2).toDouble()),
            doubleArrayOf(min(0).toDouble(), max(1).toDouble(), min(2).toDouble()),
            doubleArrayOf(min(0).toDouble(), max(1).toDouble(), max(2).toDouble()),
            doubleArrayOf(max(0).toDouble(), min(1).toDouble(), min(2).toDouble()),
            doubleArrayOf(max(0).toDouble(), min(1).toDouble(), max(2).toDouble()),
            doubleArrayOf(max(0).toDouble(), max(1).toDouble(), min(2).toDouble()),
            doubleArrayOf(max(0).toDouble(), max(1).toDouble(), max(2).toDouble()))
            .map { transform.apply(it, it); it }
    val min = points.fold(DoubleArray(3) {Double.POSITIVE_INFINITY}) {m, p -> m.indices.forEach { m[it] = Math.min(m[it], p[it]) }; m}
    val max = points.fold(DoubleArray(3) {Double.NEGATIVE_INFINITY}) {m, p -> m.indices.forEach { m[it] = Math.max(m[it], p[it]) }; m}

    return Views.interval(transformed,Intervals.smallestContainingInterval(FinalRealInterval(min, max)))
}
