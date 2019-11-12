package de.heuremo.steelmeasureapp.components.login;

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
import de.heuremo.steelmeasureapp.components.login.Registration.RegistrationSuccessfulFragment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class LoginFragmentTest {

    @Rule
    public ActivityTestRule<TestActivity> mActivityTestRule = new ActivityTestRule<TestActivity>(TestActivity.class);

    private TestActivity mActivity = null;
    private LoginFragment fragment = new LoginFragment();

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
        View view1 = fragment.getView().findViewById(R.id.customer_name_layout);
        assertNotNull(view);
        View view3 = fragment.getView().findViewById(R.id.customer_name);
        assertNotNull(view);
        View view4 = fragment.getView().findViewById(R.id.log_image);
        assertNotNull(view);
        View view5 = fragment.getView().findViewById(R.id.user_name_layout);
        assertNotNull(view);
        View view6 = fragment.getView().findViewById(R.id.user_name);
        assertNotNull(view);
        View view7 = fragment.getView().findViewById(R.id.login_password_layout);
        assertNotNull(view);
        View view8 = fragment.getView().findViewById(R.id.login_password);
        assertNotNull(view);
        View view9 = fragment.getView().findViewById(R.id.login_button);
        assertNotNull(view);
        View view10 = fragment.getView().findViewById(R.id.forget_password_button);
        assertNotNull(view);
    }

    @Test
    public void testConfirmButtonPresentInUIAndClicked() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.login_button)).perform(scrollTo(), click());
        onView(withId(R.id.forget_password_button)).perform(scrollTo(), click());
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
        fragment = null;
    }
}

