package de.heuremo.commons.common.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.heuremo.commons.common.model.order.OrderDTO;
import de.heuremo.commons.common.repository.OrderHistoryRepository;

@Singleton
public class OrderHistoryViewModel extends ViewModel {

    @Inject
    OrderHistoryRepository orderHistoryRepository;
    private MutableLiveData<List<OrderDTO>> allOrdersList = new MutableLiveData<>();

    public OrderHistoryViewModel() {

    }

    public LiveData<List<OrderDTO>> getOrders(Integer page, Integer limit) {
        //   allOrdersList = orderHistoryRepository.getAllOrders(page, limit);
        return allOrdersList;
    }


}
