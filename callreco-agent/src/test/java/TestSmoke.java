import com.williamfzc.callreco.agent.storage.CrStorage;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class TestSmoke {
    @Test
    void testCollect() {
        Sample sample = new Sample();
        sample.a();
        sample.a();

        System.out.println(Arrays.toString(
                CrStorage.INSTANCE.getProbeUnit(1L, -1).getProbes()
        ));
    }
}
