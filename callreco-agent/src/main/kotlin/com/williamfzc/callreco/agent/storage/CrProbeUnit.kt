package com.williamfzc.callreco.agent.storage

class CrProbeUnit(
    val classId: Long,
    val probesCount: Int
) {
    var probes: IntArray = IntArray(probesCount)
}