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

import net.imglib2.type.numeric.RealType

fun <T: RealType<T>> T.createVariable(value: Double): T {
    val variable = createVariable()
    variable.setReal(value)
    return variable
}

fun <T: RealType<T>> T.createVariable(value: Float): T {
    val variable = createVariable()
    variable.setReal(value)
    return variable
}

operator fun <T: RealType<T>> T.plusAssign(value: T): Unit = this.add(value)
operator fun <T: RealType<T>> T.plusAssign(value: Float): Unit = setReal(value + realDouble)
operator fun <T: RealType<T>> T.plusAssign(value: Double): Unit = setReal(value + realDouble)

operator fun <T: RealType<T>> T.plus(value: T): T = copy().let { it += value; it }
operator fun <T: RealType<T>> T.plus(value: Float): T = copy().let { it += value; it }
operator fun <T: RealType<T>> T.plus(value: Double): T = copy().let { it += value; it }
operator fun <T: RealType<T>> Float.plus(value: T): T = value + this
operator fun <T: RealType<T>> Double.plus(value: T): T = value + this


operator fun <T: RealType<T>> T.minusAssign(value: T): Unit = this.sub(value)
operator fun <T: RealType<T>> T.minusAssign(value: Float): Unit = setReal(realDouble - value)
operator fun <T: RealType<T>> T.minusAssign(value: Double): Unit = setReal(realDouble - value)

operator fun <T: RealType<T>> T.minus(value: T): T = copy().let { it -= value; it }
operator fun <T: RealType<T>> T.minus(value: Float): T = copy().let { it -= value; it }
operator fun <T: RealType<T>> T.minus(value: Double): T = copy().let { it -= value; it }
operator fun <T: RealType<T>> Float.minus(value: T): T = value.createVariable(this).let { it -= value; it }
operator fun <T: RealType<T>> Double.minus(value: T): T = value.createVariable(this).let { it -= value; it }

operator fun <T: RealType<T>> T.timesAssign(value: T): Unit = this.mul(value)
operator fun <T: RealType<T>> T.timesAssign(value: Float): Unit = setReal(value * realDouble)
operator fun <T: RealType<T>> T.timesAssign(value: Double): Unit = setReal(value * realDouble)

operator fun <T: RealType<T>> T.times(value: T): T = copy().let { it *= value; it }
operator fun <T: RealType<T>> T.times(value: Float): T = copy().let { it *= value; it }
operator fun <T: RealType<T>> T.times(value: Double): T = copy().let { it *= value; it }
operator fun <T: RealType<T>> Float.times(value: T): T = value * this
operator fun <T: RealType<T>> Double.times(value: T): T = value * this

operator fun <T: RealType<T>> T.divAssign(value: T): Unit = this.div(value)
operator fun <T: RealType<T>> T.divAssign(value: Float): Unit = setReal(realDouble / value)
operator fun <T: RealType<T>> T.divAssign(value: Double): Unit = setReal(realDouble / value)

// Cannot use this as it is shadowed by member RealType.div
//operator fun <T: RealType<T>> T.div(value: T): T = copy().let { it /= value; it }
operator fun <T: RealType<T>> T.div(value: Float): T = copy().let { it /= value; it }
operator fun <T: RealType<T>> T.div(value: Double): T = copy().let { it /= value; it }
operator fun <T: RealType<T>> Float.div(value: T): T = value.createVariable(this).let { it /= value; it }
operator fun <T: RealType<T>> Double.div(value: T): T = value.createVariable(this).let { it /= value; it }

operator fun <T: RealType<T>> T.unaryPlus() = copy()
operator fun <T: RealType<T>> T.unaryMinus() = -1.0 * this
