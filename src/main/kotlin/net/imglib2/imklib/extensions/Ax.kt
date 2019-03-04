package net.imglib2.imklib.extensions

class _Axis(val min: Long? = null, val max: Long? = null, val step: Long = 1) {

    init {
        min?.let{ max?.run { require(this >= it) } }
    }

    operator fun unaryPlus() = this
    operator fun unaryMinus() = _Axis(min = min, max = max, step = -step)
    operator fun get(min: Long? = null, max: Long? = null, step: Long = 1) = _Axis(min, max, step)
}

val AX = _Axis()

fun AX(min: Long? = null, max: Long? = null, step: Long = 1) = _Axis(min, max, step)

fun STAX(start: Long? = null, stop: Long? = null, step: Long = 1) = _Axis(min = start, max = stop?.minus(1), step = step)