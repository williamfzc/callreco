package com.williamfzc.callreco.agent.rt

import com.williamfzc.callreco.agent.storage.CrStorage

object CrRuntime {
    fun getProbes(classId: Long, probesCount: Int): IntArray {
        return CrStorage.getProbeUnit(classId, probesCount).probes
    }
}