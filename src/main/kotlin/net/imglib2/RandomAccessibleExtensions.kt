package net.imglib2

operator fun <T> RandomAccessible<T>.get(vararg pos: Long): T = randomAccess().get(*pos)

operator fun <T> RandomAccessible<T>.get(vararg pos: Int): T = randomAccess().get(*pos)

operator fun <T> RandomAccessible<T>.get(pos: Localizable): T = randomAccess()[pos]

operator fun <T> RandomAccessible<T>.get(interval: Interval, vararg pos: Long): T = randomAccess(interval).get(*pos)

operator fun <T> RandomAccessible<T>.get(interval: Interval, vararg pos: Int): T = randomAccess(interval).get(*pos)

operator fun <T> RandomAccessible<T>.get(interval: Interval, pos: Localizable): T = randomAccess(interval)[pos]