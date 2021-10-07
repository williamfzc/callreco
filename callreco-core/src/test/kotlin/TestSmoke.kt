import com.williamfzc.callreco.core.instr.CrInstrumentor
import org.junit.jupiter.api.Test
import java.io.File
import java.util.concurrent.TimeUnit

class TestSmoke {
    @Test
    fun testInst() {
        val prefix = "src/test/kotlin"
        ProcessBuilder("javac ${prefix}/Sample.java".split(" "))
            .start()
            .waitFor(15, TimeUnit.SECONDS)

        val beforeFile = File(prefix, "Sample.class")
        val afterByte = CrInstrumentor.instrument(beforeFile.readBytes())
        File(prefix, "after.class").apply {
            createNewFile()
            writeBytes(afterByte)
        }
    }
}