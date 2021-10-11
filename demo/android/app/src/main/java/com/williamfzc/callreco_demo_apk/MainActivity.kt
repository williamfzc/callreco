package com.williamfzc.callreco_demo_apk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.williamfzc.callreco.agent.rt.CrRuntime

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        a()
        Thread.sleep(200)
        b()
    }

    fun a() {
        println("a called")
    }

    fun b() {
        CrRuntime.INSTANCE.allProbes.forEach { t, u ->
            println("t: $t, u: $u")
        }
    }
}