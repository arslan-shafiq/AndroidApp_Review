package de.heuremo.steelmeasureapp.components.login;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.Observer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.heuremo.commons.common.helpers.SharedPreferenceHelper;
import de.heuremo.commons.common.model.AuthLoginDTO;
import de.heuremo.commons.common.model.AuthLoginResponeTypeEnum;
import de.heuremo.steelmeasureapp.components.ActivityMain;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;

@RunWith(MockitoJUnitRunner.class)
public class LoginFragmentTest {

    @Mock
    private LoginFragment loginFragment;

    @Test
    public void testLogIn() {
        doNothing().when(loginFragment).logIn("test", "test");
    }
}