package com.williamfzc.callreco.core.instr

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class CrCollectAdapter(cv: ClassVisitor): ClassVisitor(Opcodes.ASM7, cv) {
    private lateinit var name: String
    var methodCount: Int = 0

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        this.name = name.orEmpty()
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        methodCount ++
        return super.visitMethod(access, name, descriptor, signature, exceptions)
    }
}
