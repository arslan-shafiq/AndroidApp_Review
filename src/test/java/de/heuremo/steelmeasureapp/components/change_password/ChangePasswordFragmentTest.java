package de.heuremo.steelmeasureapp.components.change_password;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;

@RunWith(MockitoJUnitRunner.class)
public class ChangePasswordFragmentTest {

    @Mock
    private ChangePasswordFragment changePasswordFragment;

    @Test
    public void testChangePassword() {
        doNothing().when(changePasswordFragment).changePassword();
    }

    @Test
    public void testCancel() {
        doNothing().when(changePasswordFragment).cancel();
    }
}