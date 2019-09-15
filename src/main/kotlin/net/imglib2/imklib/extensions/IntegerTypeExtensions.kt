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

import net.imglib2.type.numeric.IntegerType

fun <T: IntegerType<T>> T.createVariable(value: Int): T {
    val variable = createVariable()
    variable.setInteger(value)
    return variable
}

fun <T: IntegerType<T>> T.createVariable(value: Long): T {
    val variable = createVariable()
    variable.setInteger(value)
    return variable
}

operator fun <T: IntegerType<T>> T.plusAssign(value: T): Unit = this.add(value)
operator fun <T: IntegerType<T>> T.plusAssign(value: Int): Unit = setInteger(value + integerLong)
operator fun <T: IntegerType<T>> T.plusAssign(value: Long): Unit = setInteger(value + integerLong)

operator fun <T: IntegerType<T>> T.plus(value: T): T = copy().let { it += value; it }
operator fun <T: IntegerType<T>> T.plus(value: Int): T = copy().let { it += value; it }
operator fun <T: IntegerType<T>> T.plus(value: Long): T = copy().let { it += value; it }
operator fun <T: IntegerType<T>> Int.plus(value: T): T = value + this
operator fun <T: IntegerType<T>> Long.plus(value: T): T = value + this

operator fun <T: IntegerType<T>> T.minusAssign(value: T): Unit = this.sub(value)
operator fun <T: IntegerType<T>> T.minusAssign(value: Int): Unit = setInteger(integerLong - value)
operator fun <T: IntegerType<T>> T.minusAssign(value: Long): Unit = setInteger(integerLong - value)

operator fun <T: IntegerType<T>> T.minus(value: T): T = copy().let { it -= value; it }
operator fun <T: IntegerType<T>> T.minus(value: Int): T = copy().let { it -= value; it }
operator fun <T: IntegerType<T>> T.minus(value: Long): T = copy().let { it -= value; it }
operator fun <T: IntegerType<T>> Int.minus(value: T): T = value.createVariable(this).let { it -= value; it }
operator fun <T: IntegerType<T>> Long.minus(value: T): T = value.createVariable(this).let { it -= value; it }

operator fun <T: IntegerType<T>> T.timesAssign(value: T): Unit = this.mul(value)
operator fun <T: IntegerType<T>> T.timesAssign(value: Int): Unit = setInteger(value * integerLong)
operator fun <T: IntegerType<T>> T.timesAssign(value: Long): Unit = setInteger(value * integerLong)

operator fun <T: IntegerType<T>> T.times(value: T): T = copy().let { it *= value; it }
operator fun <T: IntegerType<T>> T.times(value: Int): T = copy().let { it *= value; it }
operator fun <T: IntegerType<T>> T.times(value: Long): T = copy().let { it *= value; it }
operator fun <T: IntegerType<T>> Int.times(value: T): T = value * this
operator fun <T: IntegerType<T>> Long.times(value: T): T = value * this

operator fun <T: IntegerType<T>> T.divAssign(value: T): Unit = this.div(value)
operator fun <T: IntegerType<T>> T.divAssign(value: Int): Unit = setInteger(integerLong / value)
operator fun <T: IntegerType<T>> T.divAssign(value: Long): Unit = setInteger(integerLong / value)

// Cannot use this as it is shadowed by member IntegerType.div
//operator fun <T: IntegerType<T>> T.div(value: T): T = copy().let { it *= value; it }
operator fun <T: IntegerType<T>> T.div(value: Int): T = copy().let { it /= value; it }
operator fun <T: IntegerType<T>> T.div(value: Long): T = copy().let { it /= value; it }
operator fun <T: IntegerType<T>> Int.div(value: T): T = value.createVariable(this).let { it /= value; it }
operator fun <T: IntegerType<T>> Long.div(value: T): T = value.createVariable(this).let { it /= value; it }

operator fun <T: IntegerType<T>> T.unaryPlus() = copy()
operator fun <T: IntegerType<T>> T.unaryMinus() = -1 * this
