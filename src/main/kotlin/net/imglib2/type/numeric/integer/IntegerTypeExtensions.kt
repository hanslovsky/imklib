package net.imglib2.type.numeric.integer

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