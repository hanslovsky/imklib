package net.imglib2.examples

// import net.imglib2.* for RAI extensions
import net.imglib2.*
import net.imglib2.converter.Converters
import net.imglib2.img.array.ArrayImgs
import net.imglib2.type.numeric.real.DoubleType
import org.knowm.xchart.QuickChart
import org.knowm.xchart.SwingWrapper
import kotlin.math.sqrt

fun main() {
    val n = 1000
    val X = ArrayImgs.doubles(n.toLong()).let { it.forEachIndexed { index, t -> t.setReal(index.toDouble()) }; it } / 500.0 - 1.0
    val center = 0.3
    val sigma  = 0.4
    val sigmaSq = sigma * sigma
    val diff = center - X
    val diffSq = diff * diff
    val norm = 1.0 /  sqrt(2 * Math.PI * sigmaSq)
    val y = norm * Converters.convert(diffSq / (-2 * sigmaSq), {s, t -> t.set(Math.exp(s.realDouble))}, DoubleType())
    val chart = QuickChart.getChart(
            "Normal Distribution density function",
            "X",
            "p(X)",
            "p(X)",
            X.iterable().map { it.realDouble }.toDoubleArray(),
            y.iterable().map { it.realDouble }.toDoubleArray())
    SwingWrapper(chart).displayChart()

}