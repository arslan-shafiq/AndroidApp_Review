package de.heuremo.commons.common.viewmodel;

import androidx.lifecycle.MutableLiveData;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.heuremo.commons.common.model.user_registration.userRegistrationDTO;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationViewModelTest {

    @Mock
    private RegistrationViewModel registrationViewModel;
    private userRegistrationDTO userToRegistor;
    private MutableLiveData<userRegistrationDTO> data = new MutableLiveData<>();

    @Test
    public void testRegistration(){
        when(registrationViewModel.Registration(userToRegistor)).thenReturn(data);
    }

}