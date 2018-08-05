package com.leo.githubstars

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.DataInteraction
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import com.kona.artmining.util.waitTest
import com.leo.githubstars.ui.main.MainActivity
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.internal.matchers.TypeSafeMatcher
import org.junit.runner.RunWith


/**
 * Created by parkkh on 2018. 8. 5..
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityInstrumentationTest {


    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun searchingkun(){
        startMainActivity()
        1.waitTest()

        // Click on the search icon
        onView(withId(R.id.svGithubInput)).perform(click())

        // Type the text in the search field and submit the query
        onView(withId(R.id.svGithubInput)).perform(ViewActions.typeText("kun"))
        Espresso.closeSoftKeyboard()

        2.waitTest()
        onView(nthChildOf(withId(R.id.tvTitle), 1)).check(matches(withText("kun")))

    }

    private fun startMainActivity() {
        val intent = Intent(InstrumentationRegistry.getInstrumentation()
                .targetContext, MainActivity::class.java)

        activityTestRule.launchActivity(intent)
    }

    private fun onRow(str: String): DataInteraction {
        return onData(hasEntry(equalTo("Y.J.Kim"), `is`(str)))
    }

    fun nthChildOf(parentMatcher: Matcher<View>, childPosition: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: org.hamcrest.Description) {
                description.appendText("with $childPosition child view of type parentMatcher")
                parentMatcher.describeTo(description)
            }

            override fun matchesSafely(view: View): Boolean {
                if (view.parent !is ViewGroup) {
                    return parentMatcher.matches(view.parent)
                }

                val group = view.parent as ViewGroup
                return parentMatcher.matches(view.parent) && group.getChildAt(childPosition) == view
            }
        }
    }




}