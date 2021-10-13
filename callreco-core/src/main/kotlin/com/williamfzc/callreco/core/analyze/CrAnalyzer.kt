package com.williamfzc.callreco.core.analyze

import com.williamfzc.callreco.agent.storage.CrProbeUnit
import com.williamfzc.callreco.agent.storage.CrStorage

object CrAnalyzer : BaseMappingHandler() {
    private val runtimeData = mutableMapOf<Long, CrProbeUnit>()

    fun loadRuntimeData(data: String) {
        data.split(CrStorage.FLAG_SPLIT_LINE).forEach { eachUnit ->
            val newUnit = CrProbeUnit.load(eachUnit)
            newUnit.classId.let { clazzId ->
                runtimeData[clazzId]?.merge(newUnit)
                runtimeData.putIfAbsent(clazzId, newUnit)
            }
        }
    }
}