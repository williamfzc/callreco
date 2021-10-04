package com.williamfzc.callreco.core.rt

import com.williamfzc.callreco.core.storage.CrStorage

object CrRuntime {
    fun getProbes(classId: Long, probesCount: Int): IntArray {
        return CrStorage.getProbeUnit(classId, probesCount).probes
    }
}