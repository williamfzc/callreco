class Entry {
    fun a() {
        probes[0]++
        println("i am a")
        b()
    }

    fun b() {
        probes[1]++
        println("i am b")
        for (each in (1..10)) {
            Thread {
                c()
            }.start()
        }
    }

    fun c() {
        probes[2]++
        println("i am c")
    }

    companion object {
        val probes: IntArray = CallrecoRuntime.getProbes(1L, 3)

        @JvmStatic
        fun main(args: Array<String>) {
            Entry().a()
            println(CallrecoStorage.getProbeUnit(1L).probes.asList())
        }
    }
}