package com.williamfzc.callreco.gradle

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.williamfzc.callreco.core.ext.isJvmClass
import com.williamfzc.callreco.core.instr.CrInstrumentor
import com.williamfzc.callreco.core.log.logD
import org.gradle.api.Project
import java.io.File

class CrTransformer(private val project: Project) : Transform() {
    override fun getName(): String {
        return "CrTransformer"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean {
        return false
    }

    override fun transform(transformInvocation: TransformInvocation) {
        super.transform(transformInvocation)

        val instr = CrInstrumentor()
        val outputProvider = transformInvocation.outputProvider
        transformInvocation.inputs.forEach { eachInput ->
            eachInput.directoryInputs.forEach { eachDirInput ->
                val dest = outputProvider.getContentLocation(
                    eachDirInput.name,
                    eachDirInput.contentTypes,
                    eachDirInput.scopes,
                    Format.DIRECTORY
                )
                dest.mkdirs()
                val src = eachDirInput.file
                src.walkTopDown()
                    .filter { eachFile ->
                        eachFile.isFile
                    }
                    .forEach { eachFile ->
                        val destFile = File(eachFile.absolutePath.replace(src.absolutePath, dest.absolutePath))
                        if (eachFile.isJvmClass()) {
                            logD("instr $eachFile to $destFile")
                            val instrumented = instr.instrument(eachFile.readBytes())

                            destFile.parentFile.mkdirs()
                            destFile.writeBytes(instrumented)
                        } else {
                            eachFile.copyTo(destFile)
                        }
                    }
            }

            eachInput.jarInputs.forEach { eachJarInput ->
                val dest = outputProvider.getContentLocation(
                    eachJarInput.name,
                    eachJarInput.contentTypes,
                    eachJarInput.scopes,
                    Format.JAR
                )

                eachJarInput.file.copyTo(dest)
            }
        }
    }
}