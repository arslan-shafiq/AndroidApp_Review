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

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class OrderDetailsFragmentTest {

    @Rule
    public ActivityTestRule<TestActivity> mActivityTestRule = new ActivityTestRule<TestActivity>(TestActivity.class);

    private TestActivity mActivity = null;
    private OrderDetailsFragment fragment = new OrderDetailsFragment();

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
        LinearLayout rlContainer = (LinearLayout) mActivity.findViewById(R.id.test_container);
        assertNotNull(rlContainer);
        mActivity.getSupportFragmentManager().beginTransaction().add(rlContainer.getId(), fragment).commitAllowingStateLoss();
    }

    /*@Test
    public void testLaunch() {

        getInstrumentation().waitForIdleSync();
        View view = fragment.getView().findViewById(R.id.delivery_note_image);
        assertNotNull(view);
    }*/

    @After
    public void tearDown() throws Exception {
        mActivity = null;
        fragment = null;
    }
}