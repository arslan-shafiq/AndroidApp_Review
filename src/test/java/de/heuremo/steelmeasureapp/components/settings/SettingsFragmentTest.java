package de.heuremo.steelmeasureapp.components.settings;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;

@RunWith(MockitoJUnitRunner.class)
public class SettingsFragmentTest {

    @Mock
    SettingsFragment settingsFragment;

    @Test
    public void testOnLogOutClicked() {
        doNothing().when(settingsFragment).onLogOutClicked();
    }

    @Test
    public void testOpenTermsAndConditionsLink() {
        doNothing().when(settingsFragment).openTermsAndConditionsLink();
    }

}