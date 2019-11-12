package de.heuremo.commons.common.repository;

import androidx.lifecycle.MutableLiveData;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import de.heuremo.commons.common.model.order.OrderDTO;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderHistoryRepositoryTest {

    @Mock
    OrderHistoryRepository orderHistoryRepository;

    MutableLiveData<List<OrderDTO>> data = new MutableLiveData<>();

    @Test
    public void testCreateToken(){
        when(orderHistoryRepository.getAllOrders("test", 5)).thenReturn(data);
    }

}