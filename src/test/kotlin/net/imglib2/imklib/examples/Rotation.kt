package net.imglib2.imklib.examples

import bdv.util.BdvFunctions
import bdv.util.BdvOptions
import ij.ImagePlus
import net.imglib2.img.display.imagej.ImageJFunctions
import net.imglib2.imklib.extensions.rotate

fun main() {
    val url = "https://www.dropbox.com/s/g4i5ey9yc281dif/bfly_crop.jpeg?raw=1"
    val imp = ImagePlus(url)
    val rai = ImageJFunctions.wrapRGBA(imp)
    val rotated = rai.rotate(30.0)
    val butterflyBdv = BdvFunctions.show(rai, "butterfly", BdvOptions.options().is2D())
    BdvFunctions.show(rotated, "butterfly rotated", BdvOptions.options().addTo(butterflyBdv))
}