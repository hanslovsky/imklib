#!/usr/bin/env kscript

@file:MavenRepository("imagej.public", "https://maven.imagej.net/content/groups/public")
@file:DependsOn("net.imglib2:imglib2:5.7.0")
@file:DependsOn("net.imglib2:imklib:0.1.2-SNAPSHOT")
@file:DependsOn("sc.fiji:bigdataviewer-vistools:1.0.0-beta-13")
@file:DependsOn("net.imagej:ij:1.52k")
@file:DependsOn("net.imglib2:imglib2-ij:2.0.0-beta-45")

import bdv.util.BdvFunctions
import bdv.util.BdvOptions
import bdv.util.volatiles.VolatileViews
import ij.ImagePlus
import kotlin.math.sqrt
import net.imglib2.converter.ChannelARGBConverter
import net.imglib2.converter.Converters
import net.imglib2.converter.readwrite.ARGBChannelSamplerConverter
import net.imglib2.img.display.imagej.ImageJFunctions
import net.imglib2.imklib.extensions.*
import net.imglib2.type.numeric.ARGBType
import net.imglib2.type.numeric.integer.IntType
import net.imglib2.type.numeric.integer.UnsignedByteType
import net.imglib2.type.numeric.real.DoubleType
import net.imglib2.util.Intervals
import net.imglib2.view.Views
import kotlin.math.max
import kotlin.math.min

val url = "https://www.dropbox.com/s/g4i5ey9yc281dif/bfly_crop.jpeg?raw=1"
// this does not work, throws
// java.lang.IllegalArgumentException: No compatible service: org.scijava.service.SciJavaServic
// val butterfly = ImageJ().io().open(url)
// println("img: $butterfly")
val imp = ImagePlus(url)
val factor = 3L
val rai = ImageJFunctions.wrapRGBA(imp)[SL..factor, SL..factor]
val butterflyBdv = BdvFunctions.show(rai, "butterfly", BdvOptions.options().is2D())
val vp = butterflyBdv.bdvHandle.viewerPanel
vp.visibilityAndGrouping.isGroupingEnabled = true
vp.visibilityAndGrouping.isFusedEnabled = false
vp.visibilityAndGrouping.addSourceToGroup(0, 0)
(0 until 3).forEach {
    val channel = Converters.convert(Converters.convert(rai, ARGBChannelSamplerConverter(it + 1)), {src, tgt -> tgt.set(src.get())}, IntType())
    val extended = Views.extendBorder(channel)
    val diffs = (0 until 2).map { extended[Intervals.translate(rai, +1L, it)] - extended[Intervals.translate(rai, -1L, it)] }
    val magnitudeSquared = diffs.map {it * it }.reduce { a, b -> a + b }
    val magnitude = Converters.convert(magnitudeSquared.apply { Math.sqrt(it) }, { src, tgt -> tgt.set(min(max(src.realDouble, 0.0), 255.0).toInt())}, UnsignedByteType())
    val magnitudeARGB = Converters.convert(magnitude, ChannelARGBConverter.converterListRGBA[it], ARGBType())
    BdvFunctions.show(magnitudeARGB, "Gradient channel $it", BdvOptions.options().addTo(butterflyBdv))
    vp.visibilityAndGrouping.addSourceToGroup(it + 1, 1)
}

println("Toggle image and gradient magnitude with `1' and `2' keys")
