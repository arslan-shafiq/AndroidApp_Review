package de.heuremo.steelmeasureapp.components;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import de.heuremo.R;

import static org.junit.Assert.*;

public class ActivityMainTest {

    @Rule
    public ActivityTestRule<ActivityMain> mActivityTestRule = new ActivityTestRule<ActivityMain>(ActivityMain.class);

    private  ActivityMain mActivity = null;
    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }

    @Test
    public  void testLaunch(){
        View view = mActivity.findViewById(R.id.toolbar);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}