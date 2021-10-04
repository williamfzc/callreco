package com.williamfzc.callreco.core.instr

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

class CrInstrumentor {
    fun instrument(clazzData: ByteArray): ByteArray {
        val reader = ClassReader(clazzData)
        val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)
        val writerWrapper = CrClazzAdapter(writer)
        reader.accept(writerWrapper, ClassReader.EXPAND_FRAMES)
        return writer.toByteArray()
    }
}