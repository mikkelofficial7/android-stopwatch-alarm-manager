package com.example.simplealarmmanager

import android.util.Log
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.not
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SampleAppInstrumented {
    @Rule @JvmField
    var activityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun checkDefaultTimerByView() {
        onView(withId(R.id.tvTime)).check(matches(withText("00 : 00 : 00")))
    }

    @Test
    fun checkDefaultTimerByAssert() {
        val valueActual = activityTestRule.activity.findViewById<TextView>(R.id.tvTime).text

        println("value is: "+Assert.assertEquals("00 : 00 : 00", valueActual))
    }

        @Test
    fun checkStartAlarm() {
        onView(withId(R.id.btnStartAlarm)).perform(click())

        onView(withText("Alarm Started"))
                .inRoot(withDecorView(not(activityTestRule.activity.window.decorView)))
                .check(matches(isDisplayed()))

    }

    @Test
    fun checkStopAlarm() {
        onView(withId(R.id.btnEndAlarm)).perform(click())

        onView(withText("Alarm Cancelled"))
                .inRoot(withDecorView(not(activityTestRule.activity.window.decorView)))
                .check(matches(isDisplayed()))
    }
}