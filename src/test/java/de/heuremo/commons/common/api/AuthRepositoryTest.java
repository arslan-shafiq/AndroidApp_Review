package de.heuremo.commons.common.api;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import de.heuremo.commons.common.model.AuthLoginDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthRepositoryTest {

    @Mock
    AuthRepository authRepository;

    MutableLiveData<AuthLoginDTO> data;

    @Before
    public void setUp() throws Exception {
        data = new MutableLiveData<>();
    }

    @Test
    public void testCreateToken(){
        when(authRepository.createToken("test", "testPass")).thenReturn(data);
    }

    @Test
    public void testChangePassword(){
        when(authRepository.changePassword("testUserName", "testOldPassword", "testNewPassword")).thenReturn(data);
    }
}