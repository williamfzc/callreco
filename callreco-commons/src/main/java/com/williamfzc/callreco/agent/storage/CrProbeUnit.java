package com.williamfzc.callreco.agent.storage;

import com.williamfzc.callreco.exceptions.CrException;

import java.util.Arrays;

public class CrProbeUnit {
    private int[] probes;
    private final long classId;
    private final int probesCount;

    private final static String FLAG_SPLIT = "#";
    private final static String FLAG_SPLIT_PROBE = ",";

    public CrProbeUnit(long classId, int probesCount) {
        this(classId, probesCount, new int[probesCount]);
    }

    private CrProbeUnit(long classId, int probesCount, int[] probes) {
        this.classId = classId;
        this.probesCount = probesCount;
        this.probes = probes;
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

    public String dump() {
        if (probesCount == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(classId);
        sb.append(FLAG_SPLIT);
        for (int each : probes) {
            sb.append(each);
            sb.append(FLAG_SPLIT_PROBE);
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static CrProbeUnit load(String s) throws CrException {
        String[] parts = s.split(FLAG_SPLIT);
        if (parts.length != 2) {
            throw new CrException("format error: " + s);
        }

        long classId = Long.parseLong(parts[0]);
        int[] probes = Arrays.stream(parts[1].split(FLAG_SPLIT_PROBE))
                .mapToInt(Integer::parseInt)
                .toArray();
        int probesCount = probes.length;
        return new CrProbeUnit(classId, probesCount, probes);
    }
}
