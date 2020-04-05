package com.clingmobileapp

import android.content.Intent
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ClingApplicationLauncherTest {

    @get:Rule
    val activityRule = ActivityTestRule(ClingApplicationLauncher::class.java, false, false)

    @Test
    fun share() {
        val activity = this.activityRule.launchActivity(
                Intent(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_TEXT, "text")
        )
//        val activity = this.activityRule.launchActivity(null);
    }
}
