package net.imglib2.imklib.extensions

import net.imglib2.Localizable
import net.imglib2.RandomAccess

operator fun <T> RandomAccess<T>.get(vararg pos: Long): T {
    setPosition(pos)
    return get()
}

operator fun <T> RandomAccess<T>.get(vararg pos: Int): T {
    setPosition(pos)
    return get()
}

operator fun <T> RandomAccess<T>.get(pos: Localizable): T {
    setPosition(pos)
    return get()
}