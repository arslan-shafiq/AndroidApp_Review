package de.heuremo.steelmeasureapp.components.login.Registration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.doNothing;


@RunWith(MockitoJUnitRunner.class)
public class RegistrationFragmentTest {
    @Mock
    private RegistrationFragment registrationFragment;

    @Test
    public void testOnContinue() {
        doNothing().when(registrationFragment).onContinue();
    }

    @Test
    public void testOpenTermsAndConditionsLink() {
        doNothing().when(registrationFragment).openTermsAndConditionsLink();
    }
}