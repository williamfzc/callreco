package com.williamfzc.callreco.agent.storage;

public class CrProbeUnit {
    private int[] probes;
    private final long classId;
    private final int probesCount;

    public CrProbeUnit(long classId, int probesCount) {
        this.classId = classId;
        this.probesCount = probesCount;
        this.probes = new int[this.probesCount];
    }

    public final int[] getProbes() {
        return this.probes;
    }

    public final long getClassId() {
        return this.classId;
    }

    public final int getProbesCount() {
        return this.probesCount;
    }
}
