#!/usr/bin/env kscript

@file:MavenRepository("imagej.public", "https://maven.imagej.net/content/groups/public")
@file:DependsOn("net.imglib2:imglib2:5.6.4-SNAPSHOT")
@file:DependsOn("net.imglib2:imklib:0.1.2-SNAPSHOT")

import net.imglib2.img.array.*
import net.imglib2.imklib.extensions.*
import kotlin.random.Random


val image = ArrayImgs.ints(10, 20)
image.forEach {it.integer = Random.nextInt(1, 10)}
println("$image ${image.unique()}")
println(image.iterable().map { it.integerLong })
