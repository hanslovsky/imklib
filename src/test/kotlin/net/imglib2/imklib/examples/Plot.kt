package net.imglib2.imklib.examples

// import net.imglib2.* for RAI extensions
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