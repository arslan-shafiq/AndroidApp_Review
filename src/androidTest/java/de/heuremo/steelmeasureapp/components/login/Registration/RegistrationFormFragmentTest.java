package de.heuremo.steelmeasureapp.components.login.Registration;

import android.view.View;
import android.widget.LinearLayout;

import androidx.test.espresso.Espresso;
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
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class RegistrationFormFragmentTest {

    @Rule
    public ActivityTestRule<TestActivity> mActivityTestRule = new ActivityTestRule<TestActivity>(TestActivity.class);

    private TestActivity mActivity = null;
    private RegistrationFormFragment fragment = new RegistrationFormFragment();


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
        View view = fragment.getView().findViewById(R.id.firstname_layout);
        assertNotNull(view);
        View view1 = fragment.getView().findViewById(R.id.surname_layout);
        assertNotNull(view);
        View view3 = fragment.getView().findViewById(R.id.email_layout);
        assertNotNull(view);
        View view4 = fragment.getView().findViewById(R.id.mobile_number_layout);
        assertNotNull(view);
        View view5 = fragment.getView().findViewById(R.id.user_name_layout);
        assertNotNull(view);
        View view6 = fragment.getView().findViewById(R.id.password_layout);
        assertNotNull(view);
        View view7 = fragment.getView().findViewById(R.id.confirm_password_layout);
        assertNotNull(view);
        View view8 = fragment.getView().findViewById(R.id.firstname);
        assertNotNull(view);
        View view9 = fragment.getView().findViewById(R.id.surname);
        assertNotNull(view);
        View view10 = fragment.getView().findViewById(R.id.email);
        assertNotNull(view);
        View view11 = fragment.getView().findViewById(R.id.mobile_number);
        assertNotNull(view);
        View view12 = fragment.getView().findViewById(R.id.user_name);
        assertNotNull(view);
        View view13 = fragment.getView().findViewById(R.id.password);
        assertNotNull(view);
        View view14 = fragment.getView().findViewById(R.id.confirm_password);
        assertNotNull(view);
        View view15 = fragment.getView().findViewById(R.id.continue_button);
        assertNotNull(view);

    }

    @Test
    public void testConfirmRegistrationButtonPresentInUIAndClicked() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.continue_button)).perform(scrollTo(), click());
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
        fragment = null;
    }
}