[![](https://travis-ci.org/hanslovsky/imklib.svg?branch=master)](https://travis-ci.org/hanslovsky/imklib)

Experimental Kotlin extension functions for [imglib2](https://github.com/imglib/imglib2)

For details, check source code, tests, and [example](https://github.com/hanslovsky/imklib/blob/master/src/test/kotlin/net/imglib2/imklib/examples/Plot.kt):

```kotlin
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
```

Or, run this with [`kscript`](https://github.com/holgerbrandl/kscript):

``` kotlin
#!/usr/bin/env kscript

@file:MavenRepository("imagej.public", "https://maven.imagej.net/content/groups/public")
@file:DependsOn("net.imglib2:imglib2:5.6.4-SNAPSHOT")
@file:DependsOn("net.imglib2:imklib:0.1.1-SNAPSHOT")
@file:DependsOn("org.janelia.saalfeldlab:n5-imglib2:3.2.0")
@file:DependsOn("org.janelia.saalfeldlab:n5-hdf5:1.0.3")
@file:DependsOn("sc.fiji:bigdataviewer-vistools:1.0.0-beta-13")
@file:DependsOn("net.imagej:ij:1.52k")
@file:DependsOn("net.imglib2:imglib2-ij:2.0.0-beta-45")

import java.nio.file.Files
import java.nio.file.Paths

import bdv.util.BdvFunctions
import bdv.util.BdvOptions
import bdv.util.volatiles.VolatileViews
import ij.ImagePlus
import kotlin.math.sqrt
import kotlin.random.Random
import net.imglib2.img.array.ArrayImgs
import net.imglib2.RandomAccessibleInterval
import net.imglib2.converter.ChannelARGBConverter
import net.imglib2.converter.Converters
import net.imglib2.converter.RealDoubleConverter
import net.imglib2.converter.readwrite.ARGBChannelSamplerConverter
import net.imglib2.img.display.imagej.ImageJFunctions
import net.imglib2.imklib.extensions.*
import net.imglib2.type.numeric.ARGBType
import net.imglib2.type.numeric.integer.IntType
import net.imglib2.type.numeric.integer.UnsignedByteType
import net.imglib2.type.numeric.real.DoubleType
import net.imglib2.util.Intervals
import net.imglib2.view.Views
import org.janelia.saalfeldlab.n5.hdf5.N5HDF5Reader
import org.janelia.saalfeldlab.n5.imglib2.N5Utils
import kotlin.math.max
import kotlin.math.min

val img = ArrayImgs.doubles(1, 2, 3)

fun raiFlatToString(rai: RandomAccessibleInterval<*>) = rai.iterable().joinToString(", ", prefix="${rai::class.java.simpleName}[", postfix="]")

println(raiFlatToString(img))
img += 3.0
println(raiFlatToString(img))
val img2 = Random(100L).let { rng -> img.copy().let {copy -> copy.iterable().forEach {it.timesAssign(rng.nextDouble())}; copy}}
img[0, 1, 2].set(Double.NaN)
println(raiFlatToString(img))
println(raiFlatToString(img2))
println(raiFlatToString(-img2))
// these will only work with next imligb2 release (>5.6.3)
println(raiFlatToString(img2 / img))
println(raiFlatToString(img2.exp()))
println(raiFlatToString(img2.apply({sqrt(it)})))

val checkerboards = ArrayImgs.ints((0 until 8).map { val first = it % 2; (0 until 8).map { first - (it % 2)  }.map { it * it } }.flatten().toIntArray(), 8, 8)
println(raiFlatToString(checkerboards))
println(raiFlatToString(checkerboards[ALL, 0]))
println(raiFlatToString(checkerboards[ALL, 1]))
println(raiFlatToString(checkerboards[AX, 0][AX(0, 7, 2)]))
println(raiFlatToString(checkerboards[AX, 1][AX(0, 7, 2)]))
println(raiFlatToString(checkerboards[AX, 0][AX(1, 7, 2)]))
println(raiFlatToString(checkerboards[AX, 1][AX(1, 7, 2)]))
println(raiFlatToString(checkerboards[+AX, 0]))
println(raiFlatToString(checkerboards[-AX, 0]))

val url = "https://www.dropbox.com/s/g4i5ey9yc281dif/bfly_crop.jpeg?raw=1"
// this does not work, throws
// java.lang.IllegalArgumentException: No compatible service: org.scijava.service.SciJavaServic
// val butterfly = ImageJ().io().open(url)
// println("img: $butterfly")
val imp = ImagePlus(url)
print("imp: $imp")
val factor = 3L
val rai = ImageJFunctions.wrapRGBA(imp)[AX..factor, AX..factor]
val butterflyBdv = BdvFunctions.show(rai, "butterfly", BdvOptions.options().is2D())
val vp = butterflyBdv.bdvHandle.viewerPanel
vp.visibilityAndGrouping.isGroupingEnabled = true
vp.visibilityAndGrouping.isFusedEnabled = false
vp.visibilityAndGrouping.addSourceToGroup(0, 0)
(0 until 3).forEach {
    val channel = Converters.convert(Converters.convert(rai, ARGBChannelSamplerConverter(it + 1)), {src, tgt -> tgt.set(src.get())}, IntType())
    val extended = Views.extendBorder(channel)
    val diffs = (0 until 2).map {
        val fwd = extended[Intervals.translate(rai, +1L, it)]
        val bck = extended[Intervals.translate(rai, -1L, it)]
        fwd - bck
    }
    val diffSquared = diffs.map { it * it }
    val magnitudeSquared = diffSquared.reduce { a, b -> a + b }
    val magnitude = Converters.convert(magnitudeSquared.apply { Math.sqrt(it) }, { src, tgt -> tgt.set(min(max(src.realDouble, 0.0), 255.0).toInt())}, UnsignedByteType())
    val magnitudeARGB = Converters.convert(magnitude, ChannelARGBConverter.converterListRGBA[it], ARGBType())
    BdvFunctions.show(magnitudeARGB, "Gradient channel $it", BdvOptions.options().addTo(butterflyBdv))
    vp.visibilityAndGrouping.addSourceToGroup(it + 1, 1)
}

println("Toggle image and gradient magnitude with `1' and `2' keys")
```

