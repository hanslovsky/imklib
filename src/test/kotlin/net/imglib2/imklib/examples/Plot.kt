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
package net.imglib2.imklib.examples

// import net.imglib2.imklib.extensions.* for RAI extensions
import net.imglib2.imklib.extensions.div
import net.imglib2.imklib.extensions.exp
import net.imglib2.img.array.ArrayImgs
import net.imglib2.imklib.extensions.iterable
import net.imglib2.imklib.extensions.minus
import net.imglib2.imklib.extensions.times
import net.imglib2.imklib.extensions.unaryMinus
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler
import kotlin.math.sqrt


fun main() {
    val n = 1000
    val X = ArrayImgs.doubles(n.toLong()).let { it.forEachIndexed { index, t -> t.setReal(index.toDouble()) }; it } / n.toDouble() * 2.0 - 1.0
    val center = 0.3
    val chart = XYChartBuilder()
            .title("Density of normal distribution in interval [-1, 1] for various sigmas")
            .width(800)
            .height(600)
            .theme(Styler.ChartTheme.Matlab).build()
    for (sigma in doubleArrayOf(0.1, 0.3, 0.9, 1.5)) {
        val sigmaSq = sigma * sigma
        val diff = center - X
        val norm = 1.0 / sqrt(2 * Math.PI * sigmaSq)
        val y = norm * (-diff * diff / (2 * sigmaSq)).exp()
        chart.addSeries("Sigma=$sigma", X.iterable().map { it.realDouble }.toDoubleArray(), y.iterable().map { it.realDouble }.toDoubleArray())
    }
    SwingWrapper(chart).displayChart()

}
