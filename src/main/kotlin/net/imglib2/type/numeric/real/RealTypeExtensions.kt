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
operator fun <T: RealType<T>> T.plusAssign(value: Float): Unit = setReal(value + realFloat)
operator fun <T: RealType<T>> T.plusAssign(value: Double): Unit = setReal(value + realDouble)

operator fun <T: RealType<T>> T.plus(value: T): T = copy().let { it += value; it }
operator fun <T: RealType<T>> T.plus(value: Float): T = copy().let { it += value; it }
operator fun <T: RealType<T>> T.plus(value: Double): T = copy().let { it += value; it }