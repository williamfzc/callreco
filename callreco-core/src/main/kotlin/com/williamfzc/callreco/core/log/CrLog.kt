package com.williamfzc.callreco.core.log

const val LOG_PREFIX = "callreco"

fun realLog(level: String, msg: String) {
    println("[$LOG_PREFIX][$level] $msg")
}

fun logD(msg: String) = realLog("D", msg)
fun logI(msg: String) = realLog("I", msg)
fun logW(msg: String) = realLog("W", msg)
fun logE(msg: String) = realLog("E", msg)
