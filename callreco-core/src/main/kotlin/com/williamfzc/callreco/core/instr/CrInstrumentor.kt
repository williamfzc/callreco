package com.williamfzc.callreco.core.instr

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

object CrInstrumentor {
    fun instrument(clazzData: ByteArray): ByteArray {
        val collector = collect(clazzData)
        return modify(clazzData, collector)
    }

    private fun collect(clazzData: ByteArray): CrCollectAdapter {
        val reader = ClassReader(clazzData)
        val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)
        val collectWrapper = CrCollectAdapter(writer)
        reader.accept(collectWrapper, ClassReader.EXPAND_FRAMES)
        return collectWrapper
    }

    private fun modify(clazzData: ByteArray, collectAdapter: CrCollectAdapter): ByteArray {
        val reader = ClassReader(clazzData)
        val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)
        val writerWrapper = CrWeaveAdapter(writer, collector = collectAdapter)
        reader.accept(writerWrapper, ClassReader.EXPAND_FRAMES)
        return writer.toByteArray()
    }
}