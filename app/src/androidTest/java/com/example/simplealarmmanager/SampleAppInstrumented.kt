package com.example.simplealarmmanager

import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.*
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
open class SampleAppInstrumented {
    @Rule @JvmField
    var activityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @After
    fun checkToastShow() {
        val toast = activityTestRule.activity.toast

        toast?.let {
            println("Toast shown...")
        } ?: kotlin.run {
            println("Toast not shown...")
        }
    }

    @Test
    fun checkDefaultTimerByView() {
        onView(withId(R.id.tvTime)).check(matches(withText("00 : 00 : 00")))
    }

    @Test
    fun checkDefaultTimerByAssert() {
        val valueActual = activityTestRule.activity.findViewById<TextView>(R.id.tvTime).text
        Assert.assertEquals("00 : 00 : 00", valueActual)
    }

    @Test
    fun checkStartAlarm() {
        onView(withId(R.id.btnStartAlarm)).perform(click())
    }

    @Test
    fun checkStopAlarm() {
        onView(withId(R.id.btnEndAlarm)).perform(click())
    }
}