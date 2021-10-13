package com.williamfzc.callreco.core.instr

import com.williamfzc.callreco.core.analyze.BaseMappingHandler


object CrWeaveRecorder : BaseMappingHandler() {
    fun put(clazzId: Long, clazzInfo: CrClazzInfo) = methodMapping.put(clazzId, clazzInfo)
}
