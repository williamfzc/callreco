import com.williamfzc.callreco.agent.rt.CrRuntime
import com.williamfzc.callreco.core.analyze.CrAnalyzer
import com.williamfzc.callreco.core.instr.CrInstrumentor
import com.williamfzc.callreco.core.instr.CrWeaveRecorder
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.lang.reflect.Method
import java.net.URLClassLoader
import java.util.concurrent.TimeUnit


class TestSmoke {
    private val prefix = "src/test/kotlin"
    private val srcFile = "${prefix}/Sample.java"
    private val clzFile = "${prefix}/Sample.class"
    private val beforeClazzFile = File(clzFile)
    private val afterClazzFile = File(clzFile)

    @BeforeEach
    fun beforeTest() {
        // compile
        ProcessBuilder("javac $srcFile".split(" "))
                .start()
                .waitFor(15, TimeUnit.SECONDS)

        // weave
        val afterByte = CrInstrumentor.instrument(beforeClazzFile.readBytes())
        afterClazzFile.apply {
            createNewFile()
            writeBytes(afterByte)
        }
    }

    @Test
    fun testInst() {
        // reload weaved class
        val afterClassUrl = afterClazzFile.parentFile.toURI().toURL()
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

    @Test
    fun testPerf() {
        // reload weaved class
        val afterClassUrl = afterClazzFile.parentFile.toURI().toURL()
        val cl: ClassLoader = URLClassLoader(arrayOf(afterClassUrl))
        val afterSampleClazz = cl.loadClass("Sample")
        afterSampleClazz.declaredMethods.forEach { each ->
            println(each.toGenericString())
        }

        // load origin class
        val beforeClassUrl = beforeClazzFile.parentFile.toURI().toURL()
        val oldCl: ClassLoader = URLClassLoader(arrayOf(beforeClassUrl))
        val beforeSampleClazz = oldCl.loadClass("Sample")

        //
        val afterInst = afterSampleClazz.newInstance()
        val afterFuncB = afterSampleClazz.getDeclaredMethod("funcB")
        val beforeInst = beforeSampleClazz.newInstance()
        val beforeFuncB = beforeSampleClazz.getDeclaredMethod("funcB")

        // test
        val afterCost = pressureCall(afterFuncB, afterInst)
        val beforeCost = pressureCall(beforeFuncB, beforeInst)
        println("cost: $beforeCost -> $afterCost")
        // check CrRuntime
        val rtData = CrRuntime.INSTANCE.dump()
        println("ret: $rtData")
    }

    private fun pressureCall(m: Method, i: Any): Double {
        val start = System.currentTimeMillis()
        val count = 100000000
        for (ignored in 0..count) {
            m.invoke(i)
        }
        return (System.currentTimeMillis() - start) / (count * 1.0)
    }
}