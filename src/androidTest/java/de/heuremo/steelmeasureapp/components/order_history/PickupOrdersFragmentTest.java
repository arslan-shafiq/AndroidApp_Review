package de.heuremo.steelmeasureapp.components.order_history;

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

public class PickupOrdersFragmentTest {
    @Rule
    public ActivityTestRule<TestActivity> mActivityTestRule = new ActivityTestRule<TestActivity>(TestActivity.class);

    private TestActivity mActivity = null;
    private PickupOrdersFragment fragment = new PickupOrdersFragment();

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
        View view = fragment.getView().findViewById(R.id.recycler_view);
        assertNotNull(view);
        View view1 = fragment.getView().findViewById(R.id.order_place_date);
        assertNotNull(view);
        View view3 = fragment.getView().findViewById(R.id.Comission_text);
        assertNotNull(view);
        View view4 = fragment.getView().findViewById(R.id.order_history_image_1);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
        fragment = null;
    }
}