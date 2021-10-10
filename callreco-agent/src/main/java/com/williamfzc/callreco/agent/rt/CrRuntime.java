package com.williamfzc.callreco.agent.rt;

import com.williamfzc.callreco.agent.storage.CrStorage;

public class CrRuntime {
    public static final CrRuntime INSTANCE;

    public final int[] getProbes(long classId, int probesCount) {
        return CrStorage.INSTANCE.getProbeUnit(classId, probesCount).getProbes();
    }

    private CrRuntime() {
    }

    static {
        INSTANCE = new CrRuntime();
    }
}
