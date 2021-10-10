import com.williamfzc.callreco.agent.rt.CrRuntime;

public class Sample {
    private static transient int[] $crData;

    private static int[] $crInit() {
        int[] zArr = $crData;
        if (zArr != null) {
            return zArr;
        } else {
            int[] probes = CrRuntime.INSTANCE.getProbes(1L, 1);
            $crData = probes;
            return probes;
        }
    }

    void a() {
        $crInit()[0]++;
    }
}
