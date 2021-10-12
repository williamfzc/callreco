package com.williamfzc.callreco.agent.storage;

import java.util.HashMap;
import java.util.Map;

public class CrStorage {
    public static final CrStorage INSTANCE;

    public final static char FLAG_SPLIT_LINE = '\n';
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

    public final Map<Long, CrProbeUnit> getData() {
        return data;
    }

    public final String dump() {
        return dump(false);
    }

    public final String dump(boolean reset) {
        StringBuilder sb = new StringBuilder();
        data.values().forEach(each -> {
            sb.append(each.dump());
            sb.append(FLAG_SPLIT_LINE);
        });
        sb.setLength(sb.length() - 1);

        if (reset) {
            data.clear();
        }
        return sb.toString();
    }

    static {
        INSTANCE = new CrStorage();
    }
}