package com.williamfzc.callreco.core.storage

class CrProbeUnit(
    val classId: Long,
    val probesCount: Int
) {
    var probes: IntArray = IntArray(probesCount)
}