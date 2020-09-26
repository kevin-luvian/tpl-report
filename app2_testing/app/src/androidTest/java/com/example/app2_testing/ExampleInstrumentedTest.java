package com.example.app2_testing;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void use_app_context() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.app2_testing", appContext.getPackageName());
    }

    @Test
    public void can_edit_text() {
        onView(withId(R.id.editText1)).perform(typeText("Marco"));
        onView(withId(R.id.editText2)).perform(typeText("Polo"));
    }

    @Test
    public void can_click_button() {
        onView(withId(R.id.button)).perform(click());
    }

    @Test
    public void when_input_filled_and_button_clicked_text_is_concatenated() {
        String inp1 = "Marco";
        String inp2 = "Polo";
        String concated = MainActivity.concatString(inp1, inp2);
        onView(withId(R.id.editText1)).perform(typeText(inp1));
        onView(withId(R.id.editText2)).perform(typeText(inp2));
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.textView)).check(matches(withText(concated)));
    }
}