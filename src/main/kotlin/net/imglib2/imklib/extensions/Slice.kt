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

class Slice(val min: Long? = null, val max: Long? = null, val step: Long = 1) {

    init {
        // check that max >= min, if defined
        min?.let{ max?.run { require(this >= it) } }
    }

    operator fun unaryPlus() = this
    operator fun unaryMinus() = Slice(min = min, max = max, step = -step)
    operator fun get(min: Long? = null, max: Long? = null, step: Long = 1) = Slice(min, max, step)
    operator fun rangeTo(step: Long) = get(min = min, max = max, step = step)

    infix fun ST(step: Int) = ST(step.toLong())
    infix fun ST(step: Long) = rangeTo(step)

}

// inclusive range
infix fun Int?.IN(max: Int?) = this?.toLong() IN max

infix fun Int?.IN(max: Long?) = this?.toLong() IN max

infix fun Long?.IN(max: Int?) = this IN max?.toLong()

infix fun Long?.IN(max: Long?) = Slice(this, max)

fun IN(min: Long? = null, max: Long? = null, step: Long = 1) = min IN max ST step


// exclusive range
infix fun Int?.EX(stop: Int?) = this?.toLong() EX stop

infix fun Int?.EX(stop: Long?) = this?.toLong() EX stop

infix fun Long?.EX(stop: Int?) = this EX stop?.toLong()

infix fun Long?.EX(stop: Long?) = Slice(this, stop?.minus(1))

fun EX(start: Long? = null, stop: Long? = null, step: Long = 1) = start EX stop ST step


val SL = Slice()
