package com.williamfzc.callreco.gradle

import com.android.build.gradle.TestedExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class CrGradlePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.findByType(TestedExtension::class.java)?.let { ext ->
            println("found ext in ${target.project.path}")
            ext.registerTransform(CrTransformer(target))
        }
    }
}