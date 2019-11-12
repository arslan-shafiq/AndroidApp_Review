package de.heuremo.steelmeasureapp.components.login.Registration;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import de.heuremo.R;
import de.heuremo.steelmeasureapp.components.TestActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class RegistrationFragmentTest {

    @Rule
    public ActivityTestRule<TestActivity> mActivityTestRule = new ActivityTestRule<TestActivity>(TestActivity.class);

    private TestActivity mActivity = null;
    private RegistrationFragment fragment = new RegistrationFragment();

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
        LinearLayout rlContainer = (LinearLayout) mActivity.findViewById(R.id.test_container);
        assertNotNull(rlContainer);
        mActivity.getSupportFragmentManager().beginTransaction().add(rlContainer.getId(), fragment).commitAllowingStateLoss();
    }

    @Test
    public void testLaunch() {

        getInstrumentation().waitForIdleSync();
        View view = fragment.getView().findViewById(R.id.log_image);
        assertNotNull(view);
        View view1 = fragment.getView().findViewById(R.id.continue_button);
        assertNotNull(view);
        View view3 = fragment.getView().findViewById(R.id.invitation_key);
        assertNotNull(view);
        View view4 = fragment.getView().findViewById(R.id.customer_number);
        assertNotNull(view);
        View view5 = fragment.getView().findViewById(R.id.conditions_checkbox);
        assertNotNull(view);
        View view6 = fragment.getView().findViewById(R.id.data_protection_checkbox);
        assertNotNull(view);
    }

    @Test
    public void testConfirmButtonPresentInUIAndClicked() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.continue_button)).perform(scrollTo(), click());
    }

    @Test
    public void testCheckBoxesAreUncheck() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.data_protection_checkbox)).check(matches(isNotChecked()));
        onView(withId(R.id.conditions_checkbox)).check(matches(isNotChecked()));
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
        fragment = null;
    }
}