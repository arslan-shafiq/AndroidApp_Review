package de.heuremo.commons.common.repository;

import androidx.lifecycle.MutableLiveData;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.heuremo.commons.common.model.user_registration.userRegistrationDTO;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationRepositoryTest {

    @Mock
    private RegistrationRepository registrationRepository;

    private MutableLiveData<userRegistrationDTO> data = new MutableLiveData<>();
    private  userRegistrationDTO userToRegister;


    @Test
    public void testCreateToken(){
        when(registrationRepository.Registration(userToRegister)).thenReturn(data);
    }
}