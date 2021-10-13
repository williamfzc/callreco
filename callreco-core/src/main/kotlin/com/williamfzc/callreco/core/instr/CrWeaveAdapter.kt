package com.williamfzc.callreco.core.instr

import com.williamfzc.callreco.core.ext.dot2slash
import com.williamfzc.callreco.core.log.logD
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.commons.AdviceAdapter
import java.util.*

internal object IdGenerator {
    // same class path -> same class id
    fun calcId(s: String): Long {
        return UUID.nameUUIDFromBytes(s.toByteArray()).mostSignificantBits
    }
}

internal object CrWeaveHelper {
    const val NAME_FIELD_DATA = "\$crData"
    const val NAME_METHOD_DATA = "\$crInit"
    const val NAME_CLASS_AGENT_RT = "com.williamfzc.callreco.agent.rt.CrRuntime"
}

class CrWeaveAdapter(
        cv: ClassVisitor
) : ClassVisitor(ASM7, cv) {
    private lateinit var clazzName: String
    private var clazzId: Long = -1
    private var methodCounter: Int = 0
    private val methodList = mutableListOf<CrMethodInfo>()

    override fun visit(
            version: Int,
            access: Int,
            name: String?,
            signature: String?,
            superName: String?,
            interfaces: Array<out String>?
    ) {
        this.clazzName = name.orEmpty()
        this.clazzId = genClazzId()
        logD("weave class $clazzName, id: $clazzId")
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitEnd() {
        createDeclaration()
        // record this class
        CrWeaveRecorder.put(clazzId, CrClazzInfo(clazzId, clazzName, methodList))
        super.visitEnd()
    }

    override fun visitMethod(
            access: Int,
            name: String?,
            descriptor: String?,
            signature: String?,
            exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = cv.visitMethod(access, name, descriptor, signature, exceptions)
        val methodInfo = CrMethodInfo(name.orEmpty(), descriptor.orEmpty(), signature.orEmpty())
        methodList.add(methodInfo)
        val curMethodId = nextMethodId()
        return CrWeaveMethodAdapter(clazzName, curMethodId, api, mv, access, name, descriptor)
    }

    private fun genClazzId(): Long {
        return IdGenerator.calcId(clazzName)
    }

    private fun createCrField() {
        // private static transient int[] $crData;
        val fv = cv.visitField(
                ACC_PRIVATE + ACC_STATIC + ACC_TRANSIENT + ACC_SYNTHETIC,
                CrWeaveHelper.NAME_FIELD_DATA,
                "[I",
                null,
                null
        )
        fv.visitEnd()
    }

    private fun createCrMethod() {
        // origin method:
//        private static int[] $crInit() {
//            int[] zArr = $crData;
//            if (zArr != null) {
//                return zArr;
//            } else {
//                int[] probes = CrRuntime.getProbes(-4286205089347225830L, 7);
//                $crData = probes;
//                return probes;
//            }
//        }

        val mv = cv.visitMethod(
                ACC_PRIVATE + ACC_STATIC + ACC_SYNTHETIC,
                CrWeaveHelper.NAME_METHOD_DATA,
                "()[I",
                null,
                null
        )
        mv.visitCode()
        mv.visitFieldInsn(
                GETSTATIC,
                clazzName.dot2slash(),
                CrWeaveHelper.NAME_FIELD_DATA,
                "[I"
        )
        mv.visitVarInsn(ASTORE, 0)
        val l1 = Label()
        mv.visitLabel(l1)
        mv.visitVarInsn(ALOAD, 0)
        val l2 = Label()
        mv.visitJumpInsn(IFNULL, l2)
        mv.visitVarInsn(ALOAD, 0)
        mv.visitInsn(ARETURN)
        mv.visitLabel(l2)
        mv.visitFrame(F_NEW, 1, arrayOf<Any>("[I"), 0, null)
        mv.visitFieldInsn(
                GETSTATIC,
                CrWeaveHelper.NAME_CLASS_AGENT_RT.dot2slash(),
                "INSTANCE",
                "L${CrWeaveHelper.NAME_CLASS_AGENT_RT.dot2slash()};")
        mv.visitLdcInsn(clazzId)
        mv.pushIntInsn(methodCounter)
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                CrWeaveHelper.NAME_CLASS_AGENT_RT.dot2slash(),
                "getProbes",
                "(JI)[I",
                false
        )
        mv.visitVarInsn(ASTORE, 1)
        mv.visitVarInsn(ALOAD, 1)
        mv.visitFieldInsn(
                PUTSTATIC,
                clazzName.dot2slash(),
                CrWeaveHelper.NAME_FIELD_DATA,
                "[I"
        )
        val l4 = Label()
        mv.visitLabel(l4)
        mv.visitVarInsn(ALOAD, 1)
        mv.visitInsn(ARETURN)
        val l6 = Label()
        mv.visitLabel(l6)
        mv.visitLocalVariable("zArr", "[I", null, l1, l6, 0)
        mv.visitLocalVariable("probes", "[I", null, l4, l6, 1)
        mv.visitMaxs(3, 2)
        mv.visitEnd()
    }

    private fun createDeclaration() {
        createCrField()
        createCrMethod()
    }

    private fun nextMethodId(): Int {
        return methodCounter++
    }
}

class CrWeaveMethodAdapter(
        private val clazzName: String,
        private val methodId: Int,
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
    override fun onMethodEnter() {
        super.onMethodEnter()
        logD("enter method: $name")
        mv.visitMethodInsn(
                INVOKESTATIC,
                clazzName,
                CrWeaveHelper.NAME_METHOD_DATA,
                "()[I",
                false
        )
        mv.pushIntInsn(methodId)
        mv.visitInsn(DUP2)
        mv.visitInsn(IALOAD)
        mv.visitInsn(ICONST_1)
        mv.visitInsn(IADD)
        mv.visitInsn(IASTORE)
    }
}

private fun MethodVisitor.pushIntInsn(value: Int) {
    if (value >= -1 && value <= 5) {
        this.visitInsn(ICONST_0 + value)
    } else if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
        this.visitIntInsn(BIPUSH, value)
    } else if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
        this.visitIntInsn(SIPUSH, value)
    } else {
        this.visitLdcInsn(Integer.valueOf(value))
    }
}
