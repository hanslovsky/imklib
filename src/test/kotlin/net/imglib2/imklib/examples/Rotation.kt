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
