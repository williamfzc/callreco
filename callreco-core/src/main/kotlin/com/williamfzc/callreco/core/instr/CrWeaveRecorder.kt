package com.williamfzc.callreco.core.instr

import com.williamfzc.callreco.core.analyze.BaseMappingHandler


object CrWeaveRecorder : BaseMappingHandler() {
    fun put(clazzId: Long, clazzName: String) = methodMapping.put(clazzId, clazzName)
}
