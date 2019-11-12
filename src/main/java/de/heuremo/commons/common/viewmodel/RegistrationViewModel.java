package de.heuremo.commons.common.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.heuremo.commons.common.model.user_registration.userRegistrationDTO;
import de.heuremo.commons.common.repository.RegistrationRepository;

@Singleton
public class RegistrationViewModel extends ViewModel {

    private final MutableLiveData<String> newUser = new MutableLiveData<>();
    @Inject
    RegistrationRepository registrationRepository;

    public RegistrationViewModel() {
    }


    public LiveData<userRegistrationDTO> Registration(userRegistrationDTO userToRegistor) {
        return registrationRepository.Registration(userToRegistor);
    }
}
