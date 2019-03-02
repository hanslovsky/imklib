[![](https://travis-ci.org/hanslovsky/imglib2-kotlib.svg?branch=master)](https://travis-ci.org/hanslovsky/imglib2-kotlib)

Experimental Kotlin extension functions for [imglib2](https://github.com/imglib/imglib2)

For details, check source code, tests, and [example](https://github.com/hanslovsky/imglib2-kotlib/blob/1bb92a3227dbb320ca3af47a47d3fa222a453f3e/src/test/kotlin/net/imglib2/examples/Plot.kt):

```kotlin
package net.imglib2.examples

// import net.imglib2.* for RAI extensions
import net.imglib2.div
import net.imglib2.exp
import net.imglib2.img.array.ArrayImgs
import net.imglib2.iterable
import net.imglib2.minus
import net.imglib2.times
import net.imglib2.unaryMinus
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
```
