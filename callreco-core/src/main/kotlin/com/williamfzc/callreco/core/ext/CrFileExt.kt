package com.williamfzc.callreco.core.ext

import java.io.File

fun File.isJvmClass(): Boolean {
    return this.isFile && (this.extension == "class")
}