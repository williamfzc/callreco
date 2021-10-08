package com.williamfzc.callreco.agent.rt;

import com.williamfzc.callreco.agent.storage.CrStorage;
import org.jetbrains.annotations.NotNull;

public class CrRuntime {
    public static final CrRuntime INSTANCE;

    @NotNull
    public final int[] getProbes(long classId, int probesCount) {
        return CrStorage.INSTANCE.getProbeUnit(classId, probesCount).getProbes();
    }

    private CrRuntime() {
    }

    static {
        INSTANCE = new CrRuntime();
    }
}
