package de.heuremo.commons.common.repository;

import androidx.lifecycle.MutableLiveData;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.heuremo.commons.common.model.file_storage.FileStorageDTO;
import de.heuremo.commons.common.model.order.OrderDTO;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MakeOrderRepositoryTest {

    @Mock
    private MakeOrderRepository makeOrderRepository;

    private MutableLiveData<OrderDTO> data = new MutableLiveData<>();
    private MutableLiveData<FileStorageDTO> fileStorageDTO = new MutableLiveData<>();

    private OrderDTO orderDTO;
    private MultipartBody.Part file;
    private RequestBody name;
    private String auth;

    @Test
    public void testCreateToken(){
        when(makeOrderRepository.postOrder(orderDTO)).thenReturn(data);
    }

    @Test
    public void testUploadNotes(){
        when(makeOrderRepository.uploadNotes(file, name, auth)).thenReturn(fileStorageDTO);
    }

}