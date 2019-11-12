package de.heuremo.commons.common.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.heuremo.commons.common.api.AuthApiClient;
import de.heuremo.commons.common.model.user_registration.userRegistrationDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@Singleton
public class RegistrationRepository {
    private static final String TAG = "UserRegistrationRepository";

    private AuthApiClient userRegistrationApi;

    @Inject
    public RegistrationRepository(Retrofit retrofit) {
        this.userRegistrationApi = retrofit.create(AuthApiClient.class);
    }

    public MutableLiveData<userRegistrationDTO> Registration(userRegistrationDTO userToRegistor) {
        MutableLiveData<userRegistrationDTO> data = new MutableLiveData<>();

        userRegistrationApi
                .registration(userToRegistor)
                .enqueue(
                        new Callback<userRegistrationDTO>() {
                            @Override
                            public void onResponse(Call<userRegistrationDTO> call, Response<userRegistrationDTO> response) {
                                data.postValue(response.body());
                                Log.d(TAG, "onResponse: Call went through!");
                                Log.d(TAG, "onResponse: " + response);
                            }

                            @Override
                            public void onFailure(Call<userRegistrationDTO> call, Throwable t) {
                                data.setValue(null);
                                Log.d(TAG, "onFailure: Something went wrong calling API.");
                            }
                        });

        return data;
    }
}
