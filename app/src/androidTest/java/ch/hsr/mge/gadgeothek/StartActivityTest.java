package ch.hsr.mge.gadgeothek;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class StartActivityTest {
    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            StartActivity.class);

    @Test
    public void showsSignInForm() {
        // Todo: implement.
    }

    public void validatesEmail() {
        onView(withId(R.id.sign_in_email)).perform(typeText("invalid"), closeSoftKeyboard());
        onView(withId(R.id.sign_in_email)).check(matches(withHint("This field is required")));
    }

    public void validatesPassword() {
        onView(withId(R.id.sign_in_email)).perform(typeText("foo@example.com"), closeSoftKeyboard());
        onView(withId(R.id.sign_in_password)).check(matches(withHint("This field is required")));
    }
}
