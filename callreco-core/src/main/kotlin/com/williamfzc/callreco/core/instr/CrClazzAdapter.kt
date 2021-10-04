package com.williamfzc.callreco.core.instr

import com.williamfzc.callreco.core.log.logD
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

class CrClazzAdapter(cv: ClassVisitor): ClassVisitor(Opcodes.ASM7, cv) {
    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        logD("recv class: $name $signature $superName")
        super.visit(version, access, name, signature, superName, interfaces)
    }
}