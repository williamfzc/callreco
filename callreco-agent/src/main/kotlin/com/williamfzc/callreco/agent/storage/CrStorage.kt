package com.williamfzc.callreco.agent.storage

object CrStorage {
    private val data = mutableMapOf<Long, CrProbeUnit>()

    fun getProbeUnit(classId: Long, probesCount: Int = -1): CrProbeUnit {
        data[classId]?.let { existed ->
            return existed
        }

        CrProbeUnit(classId, probesCount).let { newUnit ->
            data[classId] = newUnit
            return newUnit
        }
    }
}