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

operator fun <T: IntegerType<T>> T.plusAssign(value: T): Unit = this.add(value)
operator fun <T: IntegerType<T>> T.plusAssign(value: Int): Unit = setInteger(value + integer)
operator fun <T: IntegerType<T>> T.plusAssign(value: Long): Unit = setInteger(value + integerLong)

operator fun <T: IntegerType<T>> T.plus(value: T): T = copy().let { it += value; it }
operator fun <T: IntegerType<T>> T.plus(value: Int): T = copy().let { it += value; it }
operator fun <T: IntegerType<T>> T.plus(value: Long): T = copy().let { it += value; it }