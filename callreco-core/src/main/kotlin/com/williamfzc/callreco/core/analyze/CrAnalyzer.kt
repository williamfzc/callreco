package com.williamfzc.callreco.core.analyze

import com.williamfzc.callreco.agent.storage.CrProbeUnit
import com.williamfzc.callreco.agent.storage.CrStorage
import com.williamfzc.callreco.core.instr.CrClazzInfo
import com.williamfzc.callreco.core.instr.CrMethodInfo

class CrAnalyzeResult(
        val data: List<CrAnalyzeClazzResult>
)

class CrAnalyzeClazzResult(
        val clazzInfo: CrClazzInfo,
        probeUnit: CrProbeUnit
) {
    var methodData: List<CrAnalyzeMethodResult>

    init {
        val tmpData = mutableListOf<CrAnalyzeMethodResult>()
        val methodList = clazzInfo.methodList
        val probesList = probeUnit.probes
        for (i in probesList.indices) {
            val curMethod = methodList[i]
            val curHitCount = probesList[i]
            tmpData.add(CrAnalyzeMethodResult(curMethod, curHitCount))
        }
        methodData = tmpData.toList()
    }
}

class CrAnalyzeMethodResult(
        val methodInfo: CrMethodInfo,
        val hitCount: Int
)

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

    fun analyze(): CrAnalyzeResult {
        val data = mutableListOf<CrAnalyzeClazzResult>()
        runtimeData.forEach { (k, v) ->
            methodMapping[k]?.let { clazzInfo ->
                val newClazzResult = CrAnalyzeClazzResult(clazzInfo, v)
                data.add(newClazzResult)
            }
        }
        return CrAnalyzeResult(data)
    }
}