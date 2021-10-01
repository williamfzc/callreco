class CallrecoProbeUnit(
    val classId: Long,
    val probesCount: Int
) {
    var probes: IntArray = IntArray(probesCount)
}

object CallrecoStorage {
    private val data = mutableMapOf<Long, CallrecoProbeUnit>()

    fun getProbeUnit(classId: Long, probesCount: Int = -1): CallrecoProbeUnit {
        data[classId]?.let { existed ->
            return existed
        }

        CallrecoProbeUnit(classId, probesCount).let { newUnit ->
            data[classId] = newUnit
            return newUnit
        }
    }
}