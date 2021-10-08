import com.williamfzc.callreco.agent.storage.CrStorage
import org.junit.jupiter.api.Test

class TestSmoke {
    @Test
    fun testCollect() {
        val sample = Sample()
        sample.a()
        sample.a()

        println(CrStorage.getProbeUnit(1L))
    }
}
