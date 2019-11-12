package de.heuremo.steelmeasureapp.components.change_password;

import android.view.View;
import android.widget.LinearLayout;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import de.heuremo.R;
import de.heuremo.steelmeasureapp.components.TestActivity;
import de.heuremo.steelmeasureapp.components.settings.SettingsFragment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class ChangePasswordFragmentTest {

    @Rule
    public ActivityTestRule<TestActivity> mActivityTestRule = new ActivityTestRule<TestActivity>(TestActivity.class);

    private TestActivity mActivity = null;
    private ChangePasswordFragment fragment = new ChangePasswordFragment();

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
        View view = fragment.getView().findViewById(R.id.old_password_layout);
        assertNotNull(view);
        View view1 = fragment.getView().findViewById(R.id.old_password);
        assertNotNull(view);
        View view3 = fragment.getView().findViewById(R.id.new_password_layout);
        assertNotNull(view);
        View view4 = fragment.getView().findViewById(R.id.new_password);
        assertNotNull(view);
        View view5 = fragment.getView().findViewById(R.id.retype_new_password_layout);
        assertNotNull(view);
        View view6 = fragment.getView().findViewById(R.id.retype_new_password);
        assertNotNull(view);
        View view7 = fragment.getView().findViewById(R.id.take_over_button);
        assertNotNull(view);
        View view8 = fragment.getView().findViewById(R.id.cancel_button);
        assertNotNull(view);
    }

    @Test
    public void testTakeOverButtonPresentInUIAndClicked() {
        onView(withId(R.id.take_over_button)).perform(closeSoftKeyboard(), scrollTo(), click());
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
        fragment = null;
    }
}