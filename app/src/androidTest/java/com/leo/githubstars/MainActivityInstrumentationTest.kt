package com.leo.githubstars

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.kona.artmining.util.waitTest
import com.leo.githubstars.ui.main.MainActivity
import com.leo.githubstars.util.RecyclerViewChildActions.Companion.childOfViewAtPositionWithMatcher
import com.leo.githubstars.util.RecyclerViewChildActions.Companion.withViewAtPosition
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by parkkh on 2018. 8. 5..
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityInstrumentationTest {

    private val testText = "leo"

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun search_leo(){
        startMainActivity()
        1.waitTest()

        onView(withId(R.id.svGithubInput)).perform(click())

        onView(withId(R.id.svGithubInput)).perform(ViewActions.typeText(testText))
        Espresso.closeSoftKeyboard()

        2.waitTest()
        onView(withId(R.id.recyclerViewGithub))
            .check(matches(childOfViewAtPositionWithMatcher(R.id.tvTitle, 0, withText(testText))))
    }

    @Test
    fun search_and_move_detail() {
        startMainActivity()
        1.waitTest()

        onView(withId(R.id.svGithubInput)).perform(click())

        onView(withId(R.id.svGithubInput)).perform(ViewActions.typeText(testText))
        Espresso.closeSoftKeyboard()

        2.waitTest()
        onView(withId(R.id.recyclerViewGithub))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(29))
        onView(withId(R.id.recyclerViewGithub))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(29, click()))
    }

    @Test
    fun search_and_detail() {
        startMainActivity()
        1.waitTest()

        onView(withId(R.id.svGithubInput)).perform(click())

        onView(withId(R.id.svGithubInput)).perform(ViewActions.typeText(testText))
        Espresso.closeSoftKeyboard()

        2.waitTest()
        onView(withId(R.id.recyclerViewGithub))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

    }

    private fun startMainActivity() {
        val intent = Intent(
            InstrumentationRegistry.getInstrumentation()
                .targetContext, MainActivity::class.java)

        activityTestRule.launchActivity(intent)
    }





}