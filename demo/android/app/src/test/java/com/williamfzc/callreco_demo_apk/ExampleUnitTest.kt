package com.williamfzc.callreco_demo_apk

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.williamfzc.callreco.agent.rt.CrRuntime
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class ExampleUnitTest {
    @Test
    fun clickingButton_shouldChangeResultsViewText() {
        MainActivity::class.java.declaredMethods.forEach {
            println(it.toGenericString())
        }
    }
}