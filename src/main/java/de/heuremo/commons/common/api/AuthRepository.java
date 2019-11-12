package de.heuremo.commons.common.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.heuremo.commons.common.model.AuthLoginDTO;
import de.heuremo.commons.common.model.AuthLoginResponeTypeEnum;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@Singleton
public class AuthRepository {

    private static final String TAG = "AuthRepository";

    private AuthApiClient authApiClient;

    @Inject
    public AuthRepository(Retrofit retrofit) {
        this.authApiClient = retrofit.create(AuthApiClient.class);
    }

    public MutableLiveData<AuthLoginDTO> createToken(String username, String password) {
        MutableLiveData<AuthLoginDTO> data = new MutableLiveData<>();

        authApiClient
                .createToken(username, password)
                .enqueue(
                        new Callback<AuthLoginDTO>() {
                            @Override
                            public void onResponse(Call<AuthLoginDTO> call, Response<AuthLoginDTO> response) {
                                data.postValue(response.body());
                                Log.d(TAG, "onResponse: Call went through!");
                                Log.d(TAG, "onResponse: " + response);
                            }

                            @Override
                            public void onFailure(Call<AuthLoginDTO> call, Throwable t) {
                                AuthLoginDTO authLoginDTO = new AuthLoginDTO(AuthLoginResponeTypeEnum.FAILURE, t.getMessage(), "");
                                data.setValue(authLoginDTO);
                                Log.d(TAG, "onFailure: Something went wrong calling API.");
                            }
                        });

        return data;
    }

    public MutableLiveData<AuthLoginDTO> changePassword(String username, String oldPassword, String newPassword) {
        MutableLiveData<AuthLoginDTO> data = new MutableLiveData<>();

        authApiClient
                .changePassword(username, oldPassword, newPassword)
                .enqueue(
                        new Callback<AuthLoginDTO>() {
                            @Override
                            public void onResponse(Call<AuthLoginDTO> call, Response<AuthLoginDTO> response) {
                                AuthLoginDTO authLoginDTO = new AuthLoginDTO(AuthLoginResponeTypeEnum.SUCCESS, "", "");
                                data.setValue(authLoginDTO);
                                Log.d(TAG, "onResponse: Call went through!");
                                Log.d(TAG, "onResponse: " + response);
                            }

                            @Override
                            public void onFailure(Call<AuthLoginDTO> call, Throwable t) {
                                AuthLoginDTO authLoginDTO = new AuthLoginDTO(AuthLoginResponeTypeEnum.FAILURE, t.getMessage(), "");
                                data.setValue(authLoginDTO);
                                Log.d(TAG, "onFailure: Something went wrong calling API.");
                            }
                        });

        return data;
    }
}
