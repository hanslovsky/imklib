package net.imglib2.type.numeric.real

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