package com.williamfzc.callreco.agent.rt;

import com.williamfzc.callreco.agent.storage.CrProbeUnit;
import com.williamfzc.callreco.agent.storage.CrStorage;

import java.util.Map;

public class CrRuntime {
    public static final CrRuntime INSTANCE;

    public final int[] getProbes(long classId, int probesCount) {
        return CrStorage.INSTANCE.getProbeUnit(classId, probesCount).getProbes();
    }

    public final Map<Long, CrProbeUnit> getAllProbes() {
        return CrStorage.INSTANCE.getData();
    }

    public final String dump(boolean reset) {
        return CrStorage.INSTANCE.dump(reset);
    }

    public final String dump() {
        return dump(false);
    }

    private CrRuntime() {
    }

    static {
        INSTANCE = new CrRuntime();
    }
}
