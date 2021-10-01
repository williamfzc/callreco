object CallrecoRuntime {
    fun getProbes(classId: Long, probesCount: Int): IntArray {
        return CallrecoStorage.getProbeUnit(classId, probesCount).probes
    }
}