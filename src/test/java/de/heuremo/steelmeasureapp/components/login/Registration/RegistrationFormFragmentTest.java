package de.heuremo.steelmeasureapp.components.login.Registration;

import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import de.heuremo.commons.common.helpers.MockEditable;
import de.heuremo.commons.common.model.user_registration.User;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationFormFragmentTest {

    @Mock
    private RegistrationFormFragment registrationFormFragment;
    @Mock
    de.heuremo.commons.common.model.user_registration.User user;

    private View view;

    @Test
    public void testValidateInputFields() {
        when(registrationFormFragment.validateInputFields()).thenReturn(true);
    }

    @Test
    public void testGetUserObjectToRegister() {
        when(registrationFormFragment.getUserObjectToRegister()).thenReturn(user);
    }

    @Test
    public void testOnSignUpButtonClick() {
        doNothing().when(registrationFormFragment).onSignUpButtonClick(view);
    }

    @Test
    public void testCloseKeyboard() {
        doNothing().when(registrationFormFragment).closeKeyboard();
    }

}