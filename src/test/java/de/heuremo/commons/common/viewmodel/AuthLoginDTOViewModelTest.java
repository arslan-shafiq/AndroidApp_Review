package de.heuremo.commons.common.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import de.heuremo.commons.common.model.AuthLoginDTO;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthLoginDTOViewModelTest {

    @Mock
    private AuthLoginDTOViewModel authLoginDTOViewModel;
    private AuthLoginDTO authLoginDTO;

    private MutableLiveData<AuthLoginDTO> data = new MutableLiveData<>();

    @Test
    public void testAuthGetData(){
       when(authLoginDTOViewModel.getData()).thenReturn(data);
    }



}