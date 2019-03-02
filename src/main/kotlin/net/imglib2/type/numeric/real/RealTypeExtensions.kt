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