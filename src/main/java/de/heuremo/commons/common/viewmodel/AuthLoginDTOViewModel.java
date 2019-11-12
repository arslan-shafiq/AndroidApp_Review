package de.heuremo.commons.common.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.heuremo.commons.common.model.AuthLoginDTO;


public class AuthLoginDTOViewModel extends ViewModel {

    public final MutableLiveData<AuthLoginDTO> data = new MutableLiveData<>();

    public LiveData<AuthLoginDTO> getData() {
        return this.data;
    }

    public void setData(AuthLoginDTO authLoginDTO) {
        this.data.setValue(authLoginDTO);
    }
}
