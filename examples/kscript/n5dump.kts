#!/usr/bin/env kscript

@file:MavenRepository("imagej.public", "https://maven.imagej.net/content/groups/public")
@file:DependsOn("net.imglib2:imglib2:5.6.4-SNAPSHOT")
@file:DependsOn("net.imglib2:imglib2-algorithm:0.11.0")
@file:DependsOn("net.imglib2:imklib:0.1.2-SNAPSHOT")
@file:DependsOn("org.janelia.saalfeldlab:n5:2.0.2")
@file:DependsOn("org.janelia.saalfeldlab:n5-imglib2:3.2.0")
@file:DependsOn("info.picocli:picocli:3.9.5")
@file:DependsOn("org.slf4j:slf4j-simple:1.7.25")

import net.imglib2.RandomAccessibleInterval
import net.imglib2.algorithm.util.Grids
import net.imglib2.transform.integer.MixedTransform
import net.imglib2.type.NativeType
import net.imglib2.type.numeric.integer.*
import net.imglib2.type.numeric.real.*
import net.imglib2.util.Intervals
import net.imglib2.view.MixedTransformView
import net.imglib2.view.Views
import org.janelia.saalfeldlab.n5.*
import org.janelia.saalfeldlab.n5.imglib2.N5Utils
import org.slf4j.LoggerFactory
import picocli.CommandLine
import java.lang.invoke.MethodHandles
import java.util.concurrent.Callable

import net.imglib2.imklib.extensions.*

class DumpN5<T : NativeType<T>>(
        val reader: N5Reader,
        val dataset: String,
        val transposeAxes: Boolean,
        val unique: Boolean)  {

    companion object {
        val LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    }

    fun dump() {
        val data = if (transposeAxes) transpose(N5Utils.open<T>(reader, dataset)) else N5Utils.open<T>(reader, dataset)
        val access = data.randomAccess()
        data.min(access)
        val min = Intervals.minAsLongArray(data)
        val max = Intervals.maxAsLongArray(data)
        val blockSize = IntArray(min.size, {1})

        val fillerStrings = Array(min.size, {if (it == 0) " " else "\n".repeat(it)})

        Grids.forEachOffset(
                min,
                max,
                blockSize,
                {pos, dim -> access.setPosition(pos, dim)},
                access::getLongPosition,
                {step: Long, dim: Int -> access.move(step, dim); print(fillerStrings[dim])},
                {print(access.get())})

    }

    private fun <U> transpose(rai: RandomAccessibleInterval<U>): RandomAccessibleInterval<U> {
        val n = rai.numDimensions()
        val component = IntArray(n, {n - 1 - it})
        val t = MixedTransform(n, n)
        t.setComponentMapping(component)
        return Views.interval(
                MixedTransformView(rai, t),
                Intervals.minAsLongArray(rai).reversedArray(),
                Intervals.maxAsLongArray(rai).reversedArray())
    }

}


@CommandLine.Command(name = "dump-n5")
class Options : Callable<Unit> {

    @CommandLine.Parameters(index="0", arity = "1", paramLabel = "CONTAINER", description = arrayOf("Path to N5 FS container"))
    var containerPath: String? = null

    @CommandLine.Parameters(index = "1", arity = "1", paramLabel = "DATASET", description = arrayOf("Dataset inside CONTAINER"))
    var dataset: String? = null

    @CommandLine.Option(names = arrayOf("--transpose-axes", "-t"))
    var transposeAxes = false

    @CommandLine.Option(names = arrayOf("--unique"))
    var unique = false

    @CommandLine.Option(names = arrayOf("--help", "-h"), usageHelp = true)
    var helpRequested = false

    var dataType: DataType? = null

    var n5: N5Reader? = null

    var parsedSuccessFully = true

    override fun call() {
        try {
            this.n5 = N5FSReader(containerPath)
            println("Container $containerPath and dataset $dataset")
            require(n5!!.datasetExists(dataset), { "Dataset $dataset does not exist in $containerPath" })
            this.dataType = this.n5?.getDatasetAttributes(this.dataset)?.dataType
        } catch(e: IllegalArgumentException) {
            DumpN5.LOG.error("Illegal argument: {}", e.message)
            parsedSuccessFully = false
        }

    }

}

val options = Options()
CommandLine.call(options, *args)

if (options.helpRequested)
    System.exit(0)

if (!options.parsedSuccessFully)
    System.exit(1)

val container = options.n5!!
val dataset = options.dataset!!

when (options.dataType!!) {
    DataType.UINT64 -> DumpN5<UnsignedLongType>(container, dataset, options.transposeAxes, options.unique)
    DataType.UINT32 -> DumpN5<UnsignedIntType>(container, dataset, options.transposeAxes, options.unique)
    DataType.UINT16 -> DumpN5<UnsignedShortType>(container, dataset, options.transposeAxes, options.unique)
    DataType.UINT8 -> DumpN5<UnsignedByteType>(container, dataset, options.transposeAxes, options.unique)
    DataType.INT64 -> DumpN5<LongType>(container, dataset, options.transposeAxes, options.unique)
    DataType.INT32 -> DumpN5<IntType>(container, dataset, options.transposeAxes, options.unique)
    DataType.INT16 -> DumpN5<ShortType>(container, dataset, options.transposeAxes, options.unique)
    DataType.INT8 -> DumpN5<ByteType>(container, dataset, options.transposeAxes, options.unique)
    DataType.FLOAT32 -> DumpN5<FloatType>(container, dataset, options.transposeAxes, options.unique)
    DataType.FLOAT64 -> DumpN5<DoubleType>(container, dataset, options.transposeAxes, options.unique)
}.dump()
