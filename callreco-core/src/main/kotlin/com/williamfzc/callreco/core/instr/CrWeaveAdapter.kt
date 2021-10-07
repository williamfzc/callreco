package com.williamfzc.callreco.core.instr

import com.williamfzc.callreco.core.log.logD
import org.objectweb.asm.commons.AdviceAdapter
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class CrWeaveAdapter(
    cv: ClassVisitor,
    val collector: CrCollectAdapter
) : ClassVisitor(Opcodes.ASM7, cv) {
    private lateinit var clazzName: String

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        this.clazzName = name.orEmpty()
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = cv.visitMethod(access, name, descriptor, signature, exceptions)
        return CrWeaveMethodAdapter(clazzName, collector, api, mv, access, name, descriptor)
    }
}

class CrWeaveMethodAdapter(
    private val clazzName: String,
    private val collector: CrCollectAdapter,
    api: Int,
    methodVisitor: MethodVisitor?,
    access: Int,
    name: String?,
    descriptor: String?
) : AdviceAdapter(
    api,
    methodVisitor,
    access,
    name,
    descriptor
) {
    override fun visitCode() {
        super.visitCode()
//        mv.visitMethodInsn(
//            Opcodes.INVOKESTATIC, clazzName,
//            "\$CrData",
//            "()[Z",
//            false
//        )
//        mv.visitVarInsn(Opcodes.ASTORE, collector.methodCount)
    }

    override fun onMethodEnter() {
        super.onMethodEnter()
        logD("enter method: $name")
//        mv.visitVarInsn(Opcodes.ALOAD, 1)
//        mv.visitInsn(Opcodes.ICONST_1)
//        mv.visitInsn(Opcodes.BASTORE)
    }
}
