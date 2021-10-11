package com.williamfzc.callreco.gradle

import com.android.build.gradle.TestedExtension
import com.williamfzc.callreco.core.instr.CrWeaveRecorder
import com.williamfzc.callreco.core.log.logE
import com.williamfzc.callreco.core.log.logI
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class CrGradlePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.findByType(TestedExtension::class.java)?.let { ext ->
            println("found ext in ${target.project.path}")
            ext.registerTransform(CrTransformer(target))
        }

        // export method mapping
        target.gradle.buildFinished {
            if (null != it.failure) {
                logE("build failed so callreco skipped")
                return@buildFinished
            }

            val outputTextFile = File(target.buildDir, "callreco-mapping.json")
            outputTextFile.createNewFile()
            outputTextFile.writeText(CrWeaveRecorder.dump())
            logI("write method mapping to $outputTextFile")
        }
    }
}