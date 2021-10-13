import com.williamfzc.callreco.agent.rt.CrRuntime
import com.williamfzc.callreco.core.analyze.CrAnalyzer
import com.williamfzc.callreco.core.instr.CrInstrumentor
import com.williamfzc.callreco.core.instr.CrWeaveRecorder
import org.junit.jupiter.api.Test
import java.io.File
import java.net.URLClassLoader
import java.util.concurrent.TimeUnit


class TestSmoke {
    @Test
    fun testInst() {
        // compile
        val prefix = "src/test/kotlin"
        val srcFile = "${prefix}/Sample.java"
        val clzFile = "${prefix}/Sample.class"
        ProcessBuilder("javac $srcFile".split(" "))
                .start()
                .waitFor(15, TimeUnit.SECONDS)

        // weave
        val beforeFile = File(clzFile)
        val afterByte = CrInstrumentor.instrument(beforeFile.readBytes())
        val afterClassFile = File(clzFile).apply {
            createNewFile()
            writeBytes(afterByte)
        }

        // reload weaved class
        val afterClassUrl = afterClassFile.parentFile.toURI().toURL()
        val cl: ClassLoader = URLClassLoader(arrayOf(afterClassUrl))
        val sampleClazz = cl.loadClass("Sample")
        sampleClazz.declaredMethods.forEach { each ->
            println(each.toGenericString())
        }
        val inst = sampleClazz.newInstance()

        // reflect call some methods
        val funcB = sampleClazz.getDeclaredMethod("funcB")
        for (ignored in 1..5) {
            funcB.invoke(inst)
        }

        // check CrRuntime
        val rtData = CrRuntime.INSTANCE.dump()
        println("ret: $rtData")

        // analyse
        val methodMapping = CrWeaveRecorder.dump()
        CrAnalyzer.load(methodMapping)
        CrAnalyzer.loadRuntimeData(rtData)
        val result = CrAnalyzer.analyze()
        result.data.forEach { each ->
            println(each.clazzInfo)
            each.methodData.forEach { eachMethod ->
                println("${eachMethod.methodInfo}, hit: ${eachMethod.hitCount}")
            }
        }
    }
}