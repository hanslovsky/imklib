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
operator fun <T: IntegerType<T>> T.plusAssign(value: Int): Unit = setInteger(value + integerLong)
operator fun <T: IntegerType<T>> T.plusAssign(value: Long): Unit = setInteger(value + integerLong)

operator fun <T: IntegerType<T>> T.plus(value: T): T = copy().let { it += value; it }
operator fun <T: IntegerType<T>> T.plus(value: Int): T = copy().let { it += value; it }
operator fun <T: IntegerType<T>> T.plus(value: Long): T = copy().let { it += value; it }

operator fun <T: IntegerType<T>> T.minusAssign(value: T): Unit = this.sub(value)
operator fun <T: IntegerType<T>> T.minusAssign(value: Int): Unit = setInteger(integerLong - value)
operator fun <T: IntegerType<T>> T.minusAssign(value: Long): Unit = setInteger(integerLong - value)

operator fun <T: IntegerType<T>> T.minus(value: T): T = copy().let { it -= value; it }
operator fun <T: IntegerType<T>> T.minus(value: Int): T = copy().let { it -= value; it }
operator fun <T: IntegerType<T>> T.minus(value: Long): T = copy().let { it -= value; it }

operator fun <T: IntegerType<T>> T.timesAssign(value: T): Unit = this.mul(value)
operator fun <T: IntegerType<T>> T.timesAssign(value: Int): Unit = setInteger(value * integerLong)
operator fun <T: IntegerType<T>> T.timesAssign(value: Long): Unit = setInteger(value * integerLong)

operator fun <T: IntegerType<T>> T.times(value: T): T = copy().let { it *= value; it }
operator fun <T: IntegerType<T>> T.times(value: Int): T = copy().let { it *= value; it }
operator fun <T: IntegerType<T>> T.times(value: Long): T = copy().let { it *= value; it }

operator fun <T: IntegerType<T>> T.divAssign(value: T): Unit = this.div(value)
operator fun <T: IntegerType<T>> T.divAssign(value: Int): Unit = setInteger(integerLong / value)
operator fun <T: IntegerType<T>> T.divAssign(value: Long): Unit = setInteger(integerLong / value)

// Cannot use this as it is shadowed by member IntegerType.div
//operator fun <T: IntegerType<T>> T.div(value: T): T = copy().let { it *= value; it }
operator fun <T: IntegerType<T>> T.div(value: Int): T = copy().let { it /= value; it }
operator fun <T: IntegerType<T>> T.div(value: Long): T = copy().let { it /= value; it }