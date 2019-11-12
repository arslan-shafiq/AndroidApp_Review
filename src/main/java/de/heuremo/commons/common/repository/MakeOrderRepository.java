package de.heuremo.commons.common.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;

import de.heuremo.commons.common.api.OrderHistoryApiClient;
import de.heuremo.commons.common.model.file_storage.FileStorageDTO;
import de.heuremo.commons.common.model.order.OrderDTO;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MakeOrderRepository {
    private static final String TAG = "MakeOrderRepository";

    private OrderHistoryApiClient orderHistoryApiClient;

    @Inject
    public MakeOrderRepository(Retrofit retrofit) {
        this.orderHistoryApiClient = retrofit.create(OrderHistoryApiClient.class);
    }

    public MutableLiveData<OrderDTO> postOrder(OrderDTO orderDTO) {
        MutableLiveData<OrderDTO> data = new MutableLiveData<>();

        orderHistoryApiClient
                .postOrder(orderDTO)
                .enqueue(
                        new Callback<OrderDTO>() {
                            @Override
                            public void onResponse(Call<OrderDTO> call, Response<OrderDTO> response) {
                                data.postValue(response.body());
                                Log.d(TAG, "onResponse: Call went through!");
                                Log.d(TAG, "onResponse: " + response);
                            }

                            @Override
                            public void onFailure(Call<OrderDTO> call, Throwable t) {
                                data.setValue(null);
                                Log.d(TAG, "onFailure: Something went wrong calling API.");
                            }
                        });

        return data;
    }

    public MutableLiveData<FileStorageDTO> uploadNotes(MultipartBody.Part file, RequestBody name, String auth) {
        MutableLiveData<FileStorageDTO> data = new MutableLiveData<>();

        orderHistoryApiClient
                .uploadNote(auth, file, name)
                .enqueue(
                        new Callback<FileStorageDTO>() {
                            @Override
                            public void onResponse(Call<FileStorageDTO> call, Response<FileStorageDTO> response) {
                                data.postValue(response.body());
                                Log.d(TAG, "onResponse: Call went through!");
                                Log.d(TAG, "onResponse: " + response);
                            }

                            @Override
                            public void onFailure(Call<FileStorageDTO> call, Throwable t) {
                                data.setValue(null);
                                Log.d(TAG, "onFailure: Something went wrong calling API.");
                            }
                        });

        return data;
    }
}
