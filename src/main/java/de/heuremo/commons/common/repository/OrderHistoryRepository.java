package de.heuremo.commons.common.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.heuremo.commons.common.api.OrderHistoryApiClient;
import de.heuremo.commons.common.model.order.OrderDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@Singleton
public class OrderHistoryRepository {

    private static final String TAG = "OrderHistoryRepository";
    private OrderHistoryApiClient orderHistoryApiClient;
    private List<OrderDTO> allOrders = new ArrayList<>();

    @Inject
    public OrderHistoryRepository(Retrofit retrofit) {
        this.orderHistoryApiClient = retrofit.create(OrderHistoryApiClient.class);
    }

    public MutableLiveData<List<OrderDTO>> getAllOrders(String auth, Integer limit) {
        MutableLiveData<List<OrderDTO>> data = new MutableLiveData<>();

        orderHistoryApiClient
                .getAllOrders(auth, limit.toString())
                .enqueue(
                        new Callback<ArrayList<OrderDTO>>() {
                            @Override
                            public void onResponse(Call<ArrayList<OrderDTO>> call, Response<ArrayList<OrderDTO>> response) {


                                // data.postValue(response.body());
                                // for demo purpose
                                loadRecyclerViewData();
                                data.setValue(allOrders);
                                Log.d(TAG, "onResponse: Call went through!");
                                Log.d(TAG, "onResponse: " + response);
                                loadRecyclerViewData();

                            }

                            @Override
                            public void onFailure(Call<ArrayList<OrderDTO>> call, Throwable t) {
                                loadRecyclerViewData();
                                data.setValue(allOrders);
                                Log.d(TAG, "onFailure: Something went wrong calling API.");
                            }
                        });


        return data;
    }


    private void loadRecyclerViewData() {

        for (int i = 0; i < 10; i++) {
            OrderDTO order = new OrderDTO();
            final int random = new Random().nextInt(10000) + 1000;
            int commision = (random + i) * i;
            order.setCommission(Integer.toString(commision));
            Integer day = 14 + i;
            Integer hours = 18 + i;
            Integer minutes = 29 + i;
            String createdDate = "Order from " + day + "-03-2019 at " + hours + ":" + minutes + " clock";
            order.setCreatedAt(createdDate);

            allOrders.add(order);
        }
    }
}
