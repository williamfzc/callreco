package com.williamfzc.callreco.agent.storage;

import java.util.HashMap;
import java.util.Map;

public class CrStorage {
    public static final CrStorage INSTANCE;

    private final Map<Long, CrProbeUnit> data = new HashMap<>();

    public CrProbeUnit getProbeUnit(long classId, int probesCount) {
        CrProbeUnit unit = data.get(classId);
        if (null != unit) {
            return unit;
        }

        // create a new one
        unit = new CrProbeUnit(classId, probesCount);
        data.put(classId, unit);
        return unit;
    }

    static {
        INSTANCE = new CrStorage();
    }
}